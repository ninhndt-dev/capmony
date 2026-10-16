package com.capmoney.api.http;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public final class HttpErrors {
    private HttpErrors() {}
    public static void write(HttpServletResponse response, int status, String code) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");
        response.getWriter().write("{\"code\":\"" + code + "\",\"message\":\"Yêu cầu chưa hoàn tất.\",\"requestId\":\"" + UUID.randomUUID() + "\"}");
    }
}
