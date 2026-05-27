package com.kbase.project.dto;

public record ProjectResponse(Long projectId, String projectName, String description, Long ownerId, Boolean active) {
}
