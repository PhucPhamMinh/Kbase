package com.kbase.user.dto;

import com.kbase.user.model.UserRole;

public record UserResponse(Long userId, String email, String fullName, UserRole role, Boolean active) {
}
