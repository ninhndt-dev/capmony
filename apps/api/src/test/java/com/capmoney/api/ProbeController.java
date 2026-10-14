package com.capmoney.api;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Packaged into the test image only. The production JAR does not contain this class.
@RestController
@RequestMapping("/api/__probe")
public class ProbeController {
    private final AtomicInteger marker = new AtomicInteger();

    @GetMapping
    public Map<String, Integer> value() { return Map.of("marker", marker.get()); }

    @PostMapping
    public Map<String, Integer> increment() { return Map.of("marker", marker.incrementAndGet()); }
}
