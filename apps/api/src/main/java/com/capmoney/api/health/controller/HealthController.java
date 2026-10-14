package com.capmoney.api.health.controller;

import com.capmoney.api.health.model.HealthStatus;
import com.capmoney.api.health.service.HealthService;
import java.util.Map;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    private final HealthService service;

    public HealthController(HealthService service) {
        this.service = service;
    }

    @GetMapping("/api/health")
    public ResponseEntity<Map<String, String>> health() {
        return result(service.health());
    }

    @GetMapping("/api/ready/auth")
    public ResponseEntity<Map<String, String>> authReadiness() {
        return result(service.authReadiness());
    }

    private ResponseEntity<Map<String, String>> result(HealthStatus status) {
        HttpStatus httpStatus = status.available() ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return ResponseEntity.status(httpStatus).cacheControl(CacheControl.noStore())
                .body(Map.of("status", status.code()));
    }
}
