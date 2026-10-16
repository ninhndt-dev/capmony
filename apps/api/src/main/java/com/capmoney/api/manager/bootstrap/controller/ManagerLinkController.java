package com.capmoney.api.manager.bootstrap.controller;

import com.capmoney.api.manager.bootstrap.service.ManagerLinkService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
@RestController
public class ManagerLinkController {
    public record LinkRequest(
            @io.swagger.v3.oas.annotations.media.Schema(accessMode = io.swagger.v3.oas.annotations.media.Schema.AccessMode.WRITE_ONLY,
                    description = "Setup token from the email fragment; never place in URL") String token) {
        @Override public String toString() { return "LinkRequest[redacted]"; }
    }
    private final ManagerLinkService service;
    public ManagerLinkController(ManagerLinkService service) { this.service = service; }

    @PostMapping(value = "/api/manager/setup-link/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> status(@RequestBody LinkRequest body, HttpServletRequest request) {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore())
                .body(Map.of("status", service.status(body.token(), (String) request.getAttribute("capmoney.clientIp")).name()));
    }

    @PostMapping(value = "/api/manager/setup-link/renew", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> renew(@RequestBody LinkRequest body, HttpServletRequest request) {
        service.renew(body.token(), (String) request.getAttribute("capmoney.clientIp"));
        return ResponseEntity.noContent().cacheControl(CacheControl.noStore()).build();
    }
}
