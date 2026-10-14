package com.capmoney.api.security.controller;

import org.springframework.http.*;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
@RestController
public class CsrfController {
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @io.swagger.v3.oas.annotations.Operation(summary = "Materialize a readable CSRF cookie", description = "XSRF-TOKEN is host-only, SameSite=Lax, Path=/ and Secure on HTTPS. Send its raw value as X-XSRF-TOKEN on unsafe requests.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "CSRF cookie materialized; no-store"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Untrusted ingress"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "Configuration/dependency unavailable")
    })
    @GetMapping("/api/auth/csrf")
    public ResponseEntity<Void> csrf(CsrfToken token) {
        token.getToken();
        return ResponseEntity.noContent().cacheControl(CacheControl.noStore()).build();
    }
}
