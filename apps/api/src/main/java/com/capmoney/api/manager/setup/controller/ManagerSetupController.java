package com.capmoney.api.manager.setup.controller;

import com.capmoney.api.manager.setup.service.ManagerSetupService;
import jakarta.servlet.http.*;
import org.springframework.http.*;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.*;

@org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
@RestController
public class ManagerSetupController {
    public record SetupRequest(
            @io.swagger.v3.oas.annotations.media.Schema(accessMode = io.swagger.v3.oas.annotations.media.Schema.AccessMode.WRITE_ONLY,
                description = "Setup token from the email fragment; 43 base64url characters", pattern = "[A-Za-z0-9_-]{43}") String token,
            @io.swagger.v3.oas.annotations.media.Schema(accessMode = io.swagger.v3.oas.annotations.media.Schema.AccessMode.WRITE_ONLY,
                description = "12–72 UTF-8 bytes; no NUL or invalid Unicode; BCrypt cost 12") String password,
            @io.swagger.v3.oas.annotations.media.Schema(accessMode = io.swagger.v3.oas.annotations.media.Schema.AccessMode.WRITE_ONLY,
                description = "Must match password exactly") String passwordConfirmation) {
        @Override public String toString() { return "SetupRequest[redacted]"; }
    }
    private final ManagerSetupService service;
    private final CookieCsrfTokenRepository csrf;
    public ManagerSetupController(ManagerSetupService service, CookieCsrfTokenRepository csrf) { this.service = service; this.csrf = csrf; }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @io.swagger.v3.oas.annotations.Operation(summary = "Complete the singleton manager setup", description = "Requires trusted edge, exact Origin and raw X-XSRF-TOKEN cookie CSRF header. JSON body is at most 4096 bytes. No session is issued. A 503 may mean the commit result is unknown; do not retry blindly.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Manager created atomically; setup token consumed"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "PASSWORD_INVALID; invalid JSON, token or password"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "CSRF_ORIGIN; no setup mutation"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "SETUP_LINK_UNAVAILABLE; wrong, expired or consumed link"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "413", description = "REQUEST_TOO_LARGE"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "415", description = "JSON_REQUIRED"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "429", description = "RATE_LIMITED; Retry-After seconds"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "DEPENDENCY_UNAVAILABLE; commit result can be unknown")
    })
    @PostMapping(value = "/api/manager/setup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> complete(@RequestBody SetupRequest body, HttpServletRequest request, HttpServletResponse response) {
        service.complete(body.token(), body.password(), body.passwordConfirmation(), (String) request.getAttribute("capmoney.clientIp"));
        csrf.saveToken(null, request, response);
        return ResponseEntity.noContent().cacheControl(CacheControl.noStore()).build();
    }
}
