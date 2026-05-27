package com.kbase.user.dto;

public record AuthResponse(String token, UserResponse user) {
}
