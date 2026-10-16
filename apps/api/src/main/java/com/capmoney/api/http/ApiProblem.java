package com.capmoney.api.http;

public class ApiProblem extends RuntimeException {
    private final int status;
    private final String code;
    private final long retryAfter;
    public ApiProblem(int status, String code) { this(status, code, 0); }
    public ApiProblem(int status, String code, long retryAfter) {
        super(code); this.status = status; this.code = code; this.retryAfter = retryAfter;
    }
    public int status() { return status; }
    public String code() { return code; }
    public long retryAfter() { return retryAfter; }
}
