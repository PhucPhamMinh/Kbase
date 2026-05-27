package com.kbase.document.dto;

public record DocumentResponse(
        Long documentId,
        Long projectId,
        Long uploadedBy,
        String title,
        String fileName,
        String fileType,
        Long fileSize,
        String storagePath,
        String mimeType,
        String description
) {
}
