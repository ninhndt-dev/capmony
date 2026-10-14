package com.capmoney.api.health.model;

public enum HealthStatus {
    UP("UP"),
    DB_UNAVAILABLE("db_unavailable"),
    REDIS_UNAVAILABLE("redis_unavailable");

    private final String code;

    HealthStatus(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    public boolean available() {
        return this == UP;
    }
}
