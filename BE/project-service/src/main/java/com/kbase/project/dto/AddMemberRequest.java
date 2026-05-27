package com.kbase.project.dto;

import com.kbase.project.model.MemberRole;
import jakarta.validation.constraints.NotNull;

public record AddMemberRequest(@NotNull Long userId, @NotNull MemberRole role) {
}
