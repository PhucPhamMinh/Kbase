package com.kbase.document.dto;

import java.time.Instant;

public record DocumentReadUrlResponse(Long documentId, String url, Instant expiresAt) {
}
