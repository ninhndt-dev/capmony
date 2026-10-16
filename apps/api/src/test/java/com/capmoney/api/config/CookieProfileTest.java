package com.capmoney.api.config;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

class CookieProfileTest {
    @Test
    void productionCannotCombineWithAnInsecureDevelopmentProfile() {
        var configuration = new SecurityConfiguration();
        for (String production : new String[]{"prod", "production"}) {
            for (String development : new String[]{"dev", "test"}) {
                var environment = new MockEnvironment().withProperty("unused", "value");
                environment.setActiveProfiles(production, development);
                assertThrows(IllegalArgumentException.class,
                    () -> configuration.csrfRepository(false, "http://127.0.0.1:28080", environment));
            }
        }
    }
    @Test
    void insecureCookiesRequireExplicitDevelopmentAndHttpsAlwaysRequiresSecure() {
        var configuration = new SecurityConfiguration();
        var production = new MockEnvironment(); production.setActiveProfiles("production");
        var development = new MockEnvironment(); development.setActiveProfiles("test");
        assertThrows(IllegalArgumentException.class, () -> configuration.csrfRepository(false, "https://capmoney.example.test", production));
        assertThrows(IllegalArgumentException.class, () -> configuration.csrfRepository(false, "http://127.0.0.1:28080", production));
        assertThrows(IllegalArgumentException.class, () -> configuration.csrfRepository(false, "https://capmoney.example.test", development));
        assertDoesNotThrow(() -> configuration.csrfRepository(true, "https://capmoney.example.test", production));
        assertDoesNotThrow(() -> configuration.csrfRepository(false, "http://127.0.0.1:28080", development));
    }
}
