package com.capmoney.api.http;

import java.util.Map;
import java.util.UUID;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiErrorAdvice {
    @ExceptionHandler(ApiProblem.class)
    public ResponseEntity<Map<String, String>> problem(ApiProblem problem) {
        var builder = ResponseEntity.status(problem.status()).cacheControl(CacheControl.noStore());
        if (problem.retryAfter() > 0) builder.header("Retry-After", Long.toString(problem.retryAfter()));
        return builder.body(body(problem.code()));
    }
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> malformed() { return problem(new ApiProblem(400, "PASSWORD_INVALID")); }
    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, String>> mediaType() { return problem(new ApiProblem(415, "JSON_REQUIRED")); }
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> method() { return problem(new ApiProblem(405, "METHOD_NOT_ALLOWED")); }
    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<Map<String, String>> missing() { return problem(new ApiProblem(404, "NOT_FOUND")); }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> dependencyFailure() {
        // A failed commit acknowledgement can mean the mutation committed. Never claim it did not.
        return problem(new ApiProblem(503, "DEPENDENCY_UNAVAILABLE"));
    }
    private Map<String, String> body(String code) {
        return Map.of("code", code, "message", "Yêu cầu chưa hoàn tất; kiểm tra lại kết quả trước khi thử tiếp.",
            "requestId", UUID.randomUUID().toString());
    }
}
