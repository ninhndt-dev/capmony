package com.capmoney.api.manager.auth.service;

import com.capmoney.api.http.ApiProblem;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
public class ManagerTokens {
    public record Claims(UUID familyId, long epoch, String jti, Instant expiresAt) {}
    private static final String ISSUER = "capmoney";
    private static final String AUDIENCE = "capmoney-web";
    private final Map<String, byte[]> keys;
    private final String activeKid;
    private final byte[] lookupKey;

    public ManagerTokens(@Value("${capmoney.jwt-key-file}") String keyPath,
            @Value("${capmoney.jwt-keyring-dir:}") String keyringDir,
            @Value("${capmoney.jwt-active-kid:v1}") String activeKid,
            @Value("${capmoney.jwt-lookup-key-file:${capmoney.jwt-key-file}}") String lookupPath) throws Exception {
        Map<String, byte[]> loaded = new HashMap<>();
        loaded.put("v1", readKey(Path.of(keyPath)));
        if (!keyringDir.isBlank()) {
            try (var files = Files.list(Path.of(keyringDir))) {
                for (Path path : files.toList()) {
                    String kid = path.getFileName().toString();
                    if (!kid.matches("v[0-9]{1,4}") || !Files.isRegularFile(path)) throw new IllegalArgumentException("Invalid JWT keyring entry");
                    loaded.put(kid, readKey(path));
                }
            }
        }
        if (!loaded.containsKey(activeKid)) throw new IllegalArgumentException("Active JWT kid is missing");
        this.keys = Map.copyOf(loaded);
        this.activeKid = activeKid;
        this.lookupKey = readKey(Path.of(lookupPath));
    }
    private static byte[] readKey(Path path) throws Exception {
        String hex = Files.readString(path, StandardCharsets.US_ASCII);
        if (!hex.matches("[0-9a-fA-F]{64}")) throw new IllegalArgumentException("JWT key must be 32 bytes in hex");
        return HexFormat.of().parseHex(hex);
    }
    public String newJti() { return UUID.randomUUID().toString(); }
    public String digest(String jti) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(lookupKey, "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal(("refresh:" + jti).getBytes(StandardCharsets.US_ASCII)));
        } catch (Exception impossible) { throw new IllegalStateException(impossible); }
    }
    public String issue(String type, UUID familyId, long epoch, String jti, Instant expiresAt) {
        if (!type.equals("access") && !type.equals("refresh")) throw new IllegalArgumentException("token type");
        try {
            Instant now = Instant.now();
            var claims = new JWTClaimsSet.Builder().issuer(ISSUER).audience(AUDIENCE).subject("manager:1")
                    .issueTime(Date.from(now)).notBeforeTime(Date.from(now)).expirationTime(Date.from(expiresAt))
                    .jwtID(jti).claim("typ", type).claim("fid", familyId.toString()).claim("epoch", epoch).build();
            var jwt = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS256).keyID(activeKid).type(JOSEObjectType.JWT).build(), claims);
            jwt.sign(new MACSigner(keys.get(activeKid)));
            return jwt.serialize();
        } catch (Exception exception) { throw new IllegalStateException("JWT signing failed", exception); }
    }
    public Claims verify(String encoded, String expectedType) {
        if (encoded == null || encoded.length() > 4096) throw new ApiProblem(401, "AUTH_REQUIRED");
        try {
            SignedJWT jwt = SignedJWT.parse(encoded);
            var header = jwt.getHeader();
            byte[] verifyKey = keys.get(header.getKeyID());
            if (!JWSAlgorithm.HS256.equals(header.getAlgorithm()) || verifyKey == null
                    || !JOSEObjectType.JWT.equals(header.getType())
                    || (header.getCriticalParams() != null && !header.getCriticalParams().isEmpty())
                    || !jwt.verify(new MACVerifier(verifyKey))) throw new IllegalArgumentException();
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            Instant now = Instant.now();
            Instant iat = Objects.requireNonNull(claims.getIssueTime()).toInstant();
            Instant nbf = Objects.requireNonNull(claims.getNotBeforeTime()).toInstant();
            Instant exp = Objects.requireNonNull(claims.getExpirationTime()).toInstant();
            long maximum = expectedType.equals("access") ? 15 * 60 : 7 * 24 * 60 * 60;
            if (!ISSUER.equals(claims.getIssuer()) || !List.of(AUDIENCE).equals(claims.getAudience())
                    || !"manager:1".equals(claims.getSubject()) || !expectedType.equals(claims.getStringClaim("typ"))
                    || iat.isAfter(now.plusSeconds(30)) || nbf.isAfter(now.plusSeconds(30)) || !exp.isAfter(now)
                    || exp.isAfter(iat.plusSeconds(maximum + 1)) || !iat.equals(nbf)) throw new IllegalArgumentException();
            UUID familyId = UUID.fromString(claims.getStringClaim("fid"));
            UUID.fromString(claims.getJWTID());
            long epoch = claims.getLongClaim("epoch");
            if (epoch < 0) throw new IllegalArgumentException();
            return new Claims(familyId, epoch, claims.getJWTID(), exp);
        } catch (Exception invalid) { throw new ApiProblem(401, "AUTH_REQUIRED"); }
    }
}
