package com.kbase.document.service;

import com.kbase.document.common.ResponseTemplate;
import com.kbase.document.dto.ProjectPermissionResponse;
import com.kbase.document.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ProjectPermissionClient {
    private final RestClient restClient;

    public ProjectPermissionClient(RestClient.Builder restClientBuilder,
                                   @Value("${services.project.base-url}") String projectServiceBaseUrl) {
        this.restClient = restClientBuilder.baseUrl(projectServiceBaseUrl).build();
    }

    public void requireRead(Long projectId, Long userId) {
        ProjectPermissionResponse permissions = permissions(projectId, userId);
        if (!Boolean.TRUE.equals(permissions.canRead())) {
            throw new BadRequestException("You do not have permission to read project documents");
        }
    }

    public void requireAdd(Long projectId, Long userId) {
        ProjectPermissionResponse permissions = permissions(projectId, userId);
        if (!Boolean.TRUE.equals(permissions.canAdd())) {
            throw new BadRequestException("You do not have permission to add project documents");
        }
    }

    public void requireModify(Long projectId, Long userId) {
        ProjectPermissionResponse permissions = permissions(projectId, userId);
        if (!Boolean.TRUE.equals(permissions.canModify())) {
            throw new BadRequestException("You do not have permission to modify project documents");
        }
    }

    public void requireDelete(Long projectId, Long userId) {
        ProjectPermissionResponse permissions = permissions(projectId, userId);
        if (!Boolean.TRUE.equals(permissions.canDelete())) {
            throw new BadRequestException("You do not have permission to delete project documents");
        }
    }

    private ProjectPermissionResponse permissions(Long projectId, Long userId) {
        try {
            ResponseTemplate<ProjectPermissionResponse> response = restClient.get()
                    .uri("/api/projects/{projectId}/permissions/{userId}", projectId, userId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<ResponseTemplate<ProjectPermissionResponse>>() {
                    });
            if (response == null || response.result() == null) {
                throw new BadRequestException("Unable to load project permissions");
            }
            return response.result();
        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BadRequestException("Unable to verify project permissions");
        }
    }
}
