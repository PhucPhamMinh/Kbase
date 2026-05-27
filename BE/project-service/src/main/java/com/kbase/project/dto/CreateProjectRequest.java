package com.kbase.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProjectRequest(@NotBlank @Size(max = 255) String projectName, @Size(max = 5000) String description) {
}
