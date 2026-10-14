package com.capmoney.api;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public final class IngressFilter extends OncePerRequestFilter {
    private static final Pattern ADDRESS_CHARS = Pattern.compile("[0-9A-Fa-f:.]+");
    private static final Pattern HEX_64 = Pattern.compile("[0-9a-fA-F]{64}");
    private final String origin;
    private final byte[] edgeSecret;
    private final byte[] subnetAddress;
    private final int subnetPrefix;

    IngressFilter(@Value("${capmoney.origin}") String origin,
                  @Value("${capmoney.api-subnet}") String subnet,
                  @Value("${capmoney.edge-secret-file}") String secretPath) throws IOException {
        if (!origin.matches("^http://127\\.0\\.0\\.1:[0-9]{2,5}$")) {
            throw new IllegalArgumentException("APP_ORIGIN must be a local Caddy loopback origin");
        }
        this.origin = origin;
        String[] cidr = subnet.split("/", -1);
        if (cidr.length != 2 || !isIp(cidr[0])) throw new IllegalArgumentException("Invalid API_SUBNET");
        this.subnetAddress = InetAddress.getByName(cidr[0]).getAddress();
        this.subnetPrefix = Integer.parseInt(cidr[1]);
        if (subnetPrefix < 0 || subnetPrefix > subnetAddress.length * 8) throw new IllegalArgumentException("Invalid API_SUBNET prefix");
        this.edgeSecret = Files.readAllBytes(Path.of(secretPath));
        if (edgeSecret.length != 64 || !HEX_64.matcher(new String(edgeSecret, StandardCharsets.US_ASCII)).matches()) {
            throw new IllegalArgumentException("Edge secret must be exactly 64 ASCII hex bytes without newline");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store");
        if (!request.getRequestURI().startsWith("/api/") || isPublicRead(request)) {
            chain.doFilter(request, response);
            return;
        }
        String peer = request.getRemoteAddr();
        String secret = singleHeader(request, "X-CM-Edge-Auth");
        String forwarded = singleHeader(request, "X-Forwarded-For");
        String real = singleHeader(request, "X-Real-IP");
        boolean valid = inSubnet(peer)
                && secret != null && MessageDigest.isEqual(edgeSecret, secret.getBytes(StandardCharsets.US_ASCII))
                && isIp(forwarded) && isIp(real) && sameIp(forwarded, real);
        if (valid && !isSafe(request.getMethod())) {
            valid = origin.equals(singleHeader(request, "Origin"));
        }
        if (!valid) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"code\":\"invalid_ingress\"}");
            return;
        }
        request.setAttribute("capmoney.clientIp", real);
        chain.doFilter(request, response);
    }

    private boolean isPublicRead(HttpServletRequest request) {
        return "GET".equals(request.getMethod()) &&
                (request.getRequestURI().equals("/api/health") || request.getRequestURI().equals("/api/ready/auth"));
    }

    private boolean isSafe(String method) {
        return List.of("GET", "HEAD", "OPTIONS").contains(method);
    }

    private String singleHeader(HttpServletRequest request, String name) {
        List<String> values = Collections.list(request.getHeaders(name));
        if (values.size() != 1) return null;
        String value = values.getFirst();
        if (value == null || value.isBlank() || !value.equals(value.trim()) || value.contains(",")) return null;
        return value;
    }

    private boolean inSubnet(String candidate) {
        if (!isIp(candidate)) return false;
        try {
            byte[] address = InetAddress.getByName(candidate).getAddress();
            if (address.length != subnetAddress.length) return false;
            int wholeBytes = subnetPrefix / 8;
            if (!Arrays.equals(Arrays.copyOf(address, wholeBytes), Arrays.copyOf(subnetAddress, wholeBytes))) return false;
            int remaining = subnetPrefix % 8;
            if (remaining == 0) return true;
            int mask = 0xff << (8 - remaining);
            return (address[wholeBytes] & mask) == (subnetAddress[wholeBytes] & mask);
        } catch (Exception exception) {
            return false;
        }
    }

    private boolean sameIp(String left, String right) {
        try {
            return Arrays.equals(InetAddress.getByName(left).getAddress(), InetAddress.getByName(right).getAddress());
        } catch (Exception exception) {
            return false;
        }
    }

    private static boolean isIp(String value) {
        if (value == null || !ADDRESS_CHARS.matcher(value).matches()) return false;
        try {
            byte[] address = InetAddress.getByName(value).getAddress();
            if (address.length == 4) {
                String[] parts = value.split("\\.", -1);
                if (parts.length != 4) return false;
                for (String part : parts) {
                    if (part.isEmpty() || part.length() > 3 || (part.length() > 1 && part.startsWith("0"))) return false;
                    int number = Integer.parseInt(part);
                    if (number < 0 || number > 255) return false;
                }
            }
            return address.length == 4 || value.contains(":");
        } catch (Exception exception) {
            return false;
        }
    }
}
