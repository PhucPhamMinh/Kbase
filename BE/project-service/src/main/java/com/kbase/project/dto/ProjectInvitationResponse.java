package com.kbase.project.dto;

import com.kbase.project.model.InvitationStatus;
import java.time.LocalDateTime;

public record ProjectInvitationResponse(
        Long invitationId,
        Long projectId,
        String invitedEmail,
        Long invitedBy,
        InvitationStatus status,
        Boolean canRead,
        Boolean canAdd,
        Boolean canModify,
        Boolean canDelete,
        LocalDateTime expiresAt,
        LocalDateTime acceptedAt
) {
}
