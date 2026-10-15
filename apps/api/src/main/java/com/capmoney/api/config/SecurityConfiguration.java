package com.capmoney.api.config;

import com.capmoney.api.http.HttpErrors;
import com.capmoney.api.security.IngressFilter;
import com.capmoney.api.security.SetupBodyFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.*;

@org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
@Configuration
public class SecurityConfiguration {
    @Bean
    CookieCsrfTokenRepository csrfRepository(@Value("${capmoney.security.cookie-secure:true}") boolean secure,
            @Value("${capmoney.origin}") String origin, Environment environment) {
        boolean development = environment.matchesProfiles("dev", "test");
        if (development && environment.matchesProfiles("prod", "production")) {
            throw new IllegalArgumentException("Production must not combine dev/test profiles");
        }
        if ((!secure || origin.startsWith("http:")) && !development) {
            throw new IllegalArgumentException("Insecure transport requires explicit dev/test profile");
        }
        if (origin.startsWith("http:") && secure) throw new IllegalArgumentException("HTTP dev/test requires COOKIE_SECURE=false");
        if (origin.startsWith("https:") && !secure) throw new IllegalArgumentException("HTTPS requires secure cookies");
        var repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookieCustomizer(cookie -> cookie.path("/").sameSite("Lax").secure(secure).httpOnly(false));
        return repository;
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, IngressFilter ingress, CookieCsrfTokenRepository csrf) throws Exception {
        var body = new SetupBodyFilter();
        http.csrf(config -> config.csrfTokenRepository(csrf).spa())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .requestCache(cache -> cache.disable())
            .httpBasic(basic -> basic.disable()).formLogin(form -> form.disable()).logout(logout -> logout.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/health", "/api/ready/auth", "/v3/api-docs", "/v3/api-docs/**", "/api/auth/csrf", "/api/auth/login", "/api/auth/refresh", "/api/auth/logout", "/api/manager/setup", "/api/manager/me", "/api/__probe").permitAll()
                .anyRequest().denyAll())
            .exceptionHandling(errors -> errors
                .accessDeniedHandler((request, response, denied) -> HttpErrors.write(response, 403, "CSRF_ORIGIN"))
                .authenticationEntryPoint((request, response, denied) -> HttpErrors.write(response, 401, "AUTH_REQUIRED")))
            .addFilterBefore(body, CsrfFilter.class)
            .addFilterBefore(ingress, SetupBodyFilter.class);
        return http.build();
    }
}
