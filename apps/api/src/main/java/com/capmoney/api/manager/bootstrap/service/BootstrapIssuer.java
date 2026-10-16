package com.capmoney.api.manager.bootstrap.service;

import com.capmoney.api.manager.bootstrap.repository.BootstrapStore;
import com.capmoney.api.http.ApiProblem;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.*;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class BootstrapIssuer {
    private final BootstrapStore store;
    private final JavaMailSender mail;
    private final Clock clock;
    private final boolean enabled;
    private final String email;
    private final String origin;
    public record Result(String code, long generation) {
        public int exitCode() { return "accepted".equals(code) ? 0 : "unknown".equals(code) ? 3 : 2; }
    }
    @org.springframework.beans.factory.annotation.Autowired
    public BootstrapIssuer(BootstrapStore store, JavaMailSender mail,
            @Value("${capmoney.bootstrap.enabled:false}") boolean enabled,
            @Value("${capmoney.bootstrap.email:}") String email,
            @Value("${capmoney.origin}") String origin) {
        this(store, mail, Clock.systemUTC(), enabled, email, origin);
    }
    BootstrapIssuer(BootstrapStore store, JavaMailSender mail, Clock clock, boolean enabled, String email, String origin) {
        this.store = store; this.mail = mail; this.clock = clock; this.enabled = enabled;
        this.email = email.trim().toLowerCase(Locale.ROOT); this.origin = origin;
    }
    public Result issue() {
        return send(null);
    }
    public Result renew(String proofDigest) {
        return send(proofDigest);
    }
    private Result send(String proofDigest) {
        if (proofDigest == null && !enabled) return new Result("disabled", 0);
        String destination;
        try { destination = proofDigest == null ? email : store.bootstrapEmail(); }
        catch (Exception unavailable) { return new Result("unknown", 0); }
        if (!validOrigin(origin) || destination == null || !destination.matches("[a-z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-z0-9](?:[a-z0-9.-]*[a-z0-9])?\\.[a-z]{2,63}")) {
            return new Result("invalid_configuration", 0);
        }
        BootstrapStore.Attempt attempt = null;
        try {
            byte[] bytes = new byte[32];
            new SecureRandom().nextBytes(bytes);
            String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            String digest = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.US_ASCII)));
            attempt = proofDigest == null ? store.reserve(destination, digest) : store.reserveRenewal(destination, proofDigest, digest);
            if (attempt == null) return new Result("unavailable", 0);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("capmoney@example.test");
            message.setTo(destination);
            message.setSubject("CapMoney: thiết lập quản lý");
            message.setText("Mở liên kết để thiết lập quản lý CapMoney:\n" + origin + "/manager/setup#token=" + token
                    + "\nLiên kết chỉ có hiệu lực sau khi nhà cung cấp chấp nhận và hệ thống xác nhận, trong 72 giờ.");
            try {
                mail.send(message);
            } catch (MailException failure) {
                // Only a connection refusal before any SMTP conversation proves no acceptance.
                String state = refusedBeforeSend(failure) ? "failed" : "unknown";
                store.fail(attempt, state);
                return new Result(state, attempt.generation());
            }
            Instant acceptedAt = clock.instant(); // Capture once, immediately after SMTP returns.
            return new Result(store.issue(attempt, acceptedAt, clock) ? "accepted" : "unknown", attempt.generation());
        } catch (ApiProblem expected) {
            throw expected;
        } catch (Exception failure) {
            if (attempt != null) {
                try { store.fail(attempt, "unknown"); } catch (Exception ignored) { /* next CLI can fence stale sending */ }
            }
            return new Result("unknown", attempt == null ? 0 : attempt.generation());
        }
    }
    private boolean validOrigin(String value) {
        try {
            var uri = java.net.URI.create(value);
            return uri.getHost() != null && uri.getRawUserInfo() == null && uri.getRawQuery() == null
                    && uri.getRawFragment() == null && (uri.getRawPath() == null || uri.getRawPath().isEmpty())
                    && ("https".equals(uri.getScheme()) || ("http".equals(uri.getScheme())
                        && "127.0.0.1".equals(uri.getHost()) && uri.getPort() > 0 && uri.getPort() <= 65535));
        } catch (IllegalArgumentException invalid) { return false; }
    }
    private boolean refusedBeforeSend(Throwable failure) {
        for (Throwable cause = failure; cause != null; cause = cause.getCause()) {
            if (cause instanceof java.net.ConnectException) return true;
            if (cause instanceof org.eclipse.angus.mail.smtp.SMTPAddressFailedException rejected
                    && rejected.getReturnCode() >= 400 && rejected.getReturnCode() < 600) return true;
            if (cause instanceof org.eclipse.angus.mail.smtp.SMTPSendFailedException rejected
                    && rejected.getReturnCode() >= 400 && rejected.getReturnCode() < 600) return true;
            if (cause instanceof MailSendException send) {
                for (Exception nested : send.getMessageExceptions()) {
                    if (nested != cause && refusedBeforeSend(nested)) return true;
                }
            }
            if (cause instanceof jakarta.mail.MessagingException smtp && smtp.getNextException() != null
                    && smtp.getNextException() != cause && refusedBeforeSend(smtp.getNextException())) return true;
        }
        return false;
    }
}
