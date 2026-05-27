package com.kbase.document.dto;

public record ProjectPermissionResponse(
        Long projectId,
        Long userId,
        Boolean owner,
        Boolean canRead,
        Boolean canAdd,
        Boolean canModify,
        Boolean canDelete
) {
}
