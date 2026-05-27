package com.kbase.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteMemberRequest(
        @NotBlank @Email String email,
        @NotNull Boolean canRead,
        @NotNull Boolean canAdd,
        @NotNull Boolean canModify,
        @NotNull Boolean canDelete
) {
}
