package com.capmoney.api;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.net.ServerSocket;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

class MailpitMailTest {
    @Test
    void refusedSmtpDoesNotReportSuccess() throws Exception {
        int port;
        try (ServerSocket reservation = new ServerSocket(0)) {
            port = reservation.getLocalPort();
        }
        assertThrows(MailSendException.class, () -> sender("127.0.0.1", port).send(message("refused")));
    }

    @Test
    void silentSmtpTimesOutWithoutReportingSuccess() throws Exception {
        try (ServerSocket silent = new ServerSocket(0)) {
            long started = System.nanoTime();
            assertThrows(MailSendException.class,
                    () -> sender("127.0.0.1", silent.getLocalPort()).send(message("timeout")));
            assertTrue(Duration.ofNanos(System.nanoTime() - started).toSeconds() < 5,
                    "SMTP timeout must be bounded");
        }
    }

    private JavaMailSenderImpl sender(String host, int port) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        Properties properties = sender.getJavaMailProperties();
        properties.setProperty("mail.smtp.connectiontimeout", "1000");
        properties.setProperty("mail.smtp.timeout", "1000");
        properties.setProperty("mail.smtp.writetimeout", "1000");
        return sender;
    }

    private SimpleMailMessage message(String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("infra@example.test");
        message.setTo("manager@example.test");
        message.setSubject(subject);
        message.setText("Isolated CapMoney infrastructure test");
        return message;
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "SMTP_HOST", matches = ".+")
    void sendsThroughJavaMailSenderAndConfirmsMailpitReceivedIt() throws Exception {
        String host = System.getenv("SMTP_HOST");
        String subject = "capmoney-infra-" + UUID.randomUUID();
        sender(host, Integer.parseInt(System.getenv().getOrDefault("SMTP_PORT", "1025")))
                .send(message(subject));

        var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
        var request = HttpRequest.newBuilder(URI.create("http://" + host + ":8025/api/v1/messages"))
                .timeout(Duration.ofSeconds(2)).GET().build();
        boolean received = false;
        for (int attempt = 0; attempt < 10 && !received; attempt++) {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            received = response.statusCode() == 200 && response.body().contains(subject)
                    && response.body().contains("manager@example.test");
            if (!received) Thread.sleep(100);
        }
        assertTrue(received, "SMTP accepted but Mailpit receipt is unverified");
    }
}
