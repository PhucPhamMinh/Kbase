package com.kbase.user.common;

import java.time.Instant;

public record ResponseTemplate<T>(int status, String message, T result, Object metadata, Instant timestamp) {
    public static <T> ResponseTemplate<T> success(String message, T result) {
        return new ResponseTemplate<>(200, message, result, null, Instant.now());
    }

    public static <T> ResponseTemplate<T> error(int status, String message, Object metadata) {
        return new ResponseTemplate<>(status, message, null, metadata, Instant.now());
    }
}
