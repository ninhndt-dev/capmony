package com.capmoney.api.manager.auth;

import static org.junit.jupiter.api.Assertions.*;
import com.capmoney.api.http.ApiProblem;
import com.capmoney.api.manager.auth.service.ManagerTokens;
import java.nio.file.*;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ManagerTokensTest {
    @Test
    void rotatedSignerKeepsOldKeyForVerificationAndStableRefreshLookup() throws Exception {
        Path directory = Files.createTempDirectory("capmoney-jwt-keys-");
        Path first = directory.resolve("first");
        Path lookup = directory.resolve("lookup");
        Path keyring = Files.createDirectory(directory.resolve("keyring"));
        Files.writeString(first, "a".repeat(64));
        Files.writeString(lookup, "b".repeat(64));
        Files.writeString(keyring.resolve("v2"), "c".repeat(64));
        var before = new ManagerTokens(first.toString(), "", "v1", lookup.toString());
        var after = new ManagerTokens(first.toString(), keyring.toString(), "v2", lookup.toString());
        UUID family = UUID.randomUUID();
        String old = before.issue("refresh", family, 2, before.newJti(), Instant.now().plusSeconds(600));
        String current = after.issue("access", family, 2, after.newJti(), Instant.now().plusSeconds(600));
        assertEquals(family, after.verify(old, "refresh").familyId());
        assertEquals(family, after.verify(current, "access").familyId());
        assertEquals(before.digest("a-jti"), after.digest("a-jti"));
        assertThrows(ApiProblem.class, () -> before.verify(current, "access"));
        assertThrows(ApiProblem.class, () -> after.verify(old, "access"));
        assertThrows(ApiProblem.class, () -> after.verify(after.issue("access", family, 2,
                after.newJti(), Instant.now().minusSeconds(1)), "access"));
        assertThrows(ApiProblem.class, () -> after.verify(after.issue("access", family, 2,
                after.newJti(), Instant.now().plusSeconds(3600)), "access"));
    }
}
