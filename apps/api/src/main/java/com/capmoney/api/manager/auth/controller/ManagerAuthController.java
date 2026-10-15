package com.capmoney.api.manager.auth.controller;

import com.capmoney.api.http.ApiProblem;
import com.capmoney.api.manager.auth.service.ManagerAuthService;
import jakarta.servlet.http.*;
import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.*;

@org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
@RestController
public class ManagerAuthController {
    public record LoginRequest(String email, String password) {
        @Override public String toString() { return "LoginRequest[redacted]"; }
    }
    private final ManagerAuthService service;
    private final CookieCsrfTokenRepository csrf;
    private final boolean secure;
    private final String accessName;
    private final String refreshName;
    public ManagerAuthController(ManagerAuthService service, CookieCsrfTokenRepository csrf,
            @Value("${capmoney.security.cookie-secure:true}") boolean secure, Environment environment) {
        this.service = service; this.csrf = csrf; this.secure = secure;
        boolean development = environment.matchesProfiles("dev", "test");
        accessName = development ? "cm_dev_access" : "cm_access";
        refreshName = development ? "cm_dev_refresh" : "cm_refresh";
    }
    @PostMapping(value = "/api/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @io.swagger.v3.oas.annotations.Operation(summary = "Sign in as the singleton manager", description = "Requires trusted edge, exact Origin and X-XSRF-TOKEN. Sets HttpOnly access and refresh cookies; no-store.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Manager session issued"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "LOGIN_FAILED"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "CSRF_ORIGIN"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "429", description = "RATE_LIMITED"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "DEPENDENCY_UNAVAILABLE")
    })
    public ResponseEntity<Void> login(@RequestBody LoginRequest body, HttpServletRequest request, HttpServletResponse response) {
        var pair = service.login(body.email(), body.password(), (String) request.getAttribute("capmoney.clientIp"));
        setPair(response, pair);
        csrf.saveToken(null, request, response);
        return ResponseEntity.noContent().cacheControl(CacheControl.noStore()).build();
    }
    @PostMapping("/api/auth/refresh")
    @io.swagger.v3.oas.annotations.Operation(summary = "Rotate a manager refresh cookie once", description = "A reused valid refresh token revokes only its family. Never retry a refresh POST after an unknown network result.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Access and refresh cookies rotated"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "AUTH_REQUIRED or REFRESH_REUSED"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "CSRF_ORIGIN"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "DEPENDENCY_UNAVAILABLE")
    })
    public ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
        try {
            var pair = service.refresh(cookie(request, refreshName));
            setPair(response, pair);
            csrf.saveToken(null, request, response);
            return ResponseEntity.noContent().cacheControl(CacheControl.noStore()).build();
        } catch (ApiProblem rejected) {
            if (rejected.status() == 401) clearCookies(response);
            throw rejected;
        }
    }
    @PostMapping("/api/auth/logout")
    @io.swagger.v3.oas.annotations.Operation(summary = "Revoke every manager session", description = "A valid access cookie has priority over a stale refresh cookie. Unknown revocation result must not be reported as success.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "All manager sessions revoked"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "AUTH_REQUIRED or REFRESH_REUSED"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "CSRF_ORIGIN"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "REVOCATION_UNKNOWN")
    })
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            var result = service.logout(cookie(request, accessName), cookie(request, refreshName));
            if (!result.allRevoked()) throw new ApiProblem(401, result.code());
            return ResponseEntity.noContent().cacheControl(CacheControl.noStore()).build();
        } catch (ApiProblem expected) {
            throw expected;
        } catch (Exception unavailable) {
            throw new ApiProblem(503, "REVOCATION_UNKNOWN");
        } finally {
            clearCookies(response);
            csrf.saveToken(null, request, response);
        }
    }
    @GetMapping("/api/manager/me")
    @io.swagger.v3.oas.annotations.Operation(summary = "Read current manager identity", description = "Access cookie and current database epoch/family are required on every request.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Email and MANAGER role"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "AUTH_REQUIRED"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "DEPENDENCY_UNAVAILABLE")
    })
    public ResponseEntity<Map<String, String>> me(HttpServletRequest request) {
        String email = service.me(cookie(request, accessName));
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(Map.of("email", email, "role", "MANAGER"));
    }
    private String cookie(HttpServletRequest request, String name) {
        String value = null;
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName())) {
                if (value != null) throw new ApiProblem(401, "AUTH_REQUIRED");
                value = cookie.getValue();
            }
        }
        return value;
    }
    private void setPair(HttpServletResponse response, ManagerAuthService.Pair pair) {
        response.addHeader(HttpHeaders.SET_COOKIE, authCookie(accessName, pair.access(), "/api", Duration.ofMinutes(15)).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, authCookie(refreshName, pair.refresh(), "/api/auth", Duration.ofSeconds(pair.refreshMaxAgeSeconds())).toString());
    }
    private void clearCookies(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, authCookie(accessName, "", "/api", Duration.ZERO).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, authCookie(refreshName, "", "/api/auth", Duration.ZERO).toString());
    }
    private ResponseCookie authCookie(String name, String value, String path, Duration age) {
        return ResponseCookie.from(name, value).httpOnly(true).secure(secure).sameSite("Lax").path(path).maxAge(age).build();
    }
}
