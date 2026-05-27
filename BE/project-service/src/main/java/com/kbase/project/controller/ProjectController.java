package com.kbase.project.controller;

import com.kbase.project.common.ResponseTemplate;
import com.kbase.project.dto.AddMemberRequest;
import com.kbase.project.dto.CreateProjectRequest;
import com.kbase.project.dto.InviteMemberRequest;
import com.kbase.project.dto.ProjectInvitationResponse;
import com.kbase.project.dto.ProjectMemberResponse;
import com.kbase.project.dto.ProjectPermissionResponse;
import com.kbase.project.dto.ProjectResponse;
import com.kbase.project.dto.UpdateProjectRequest;
import com.kbase.project.service.ProjectService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseTemplate<ProjectResponse> create(@RequestHeader("X-User-Id") Long userId,
                                                    @Valid @RequestBody CreateProjectRequest request) {
        return ResponseTemplate.success("Project created", projectService.create(request, userId));
    }

    @GetMapping
    public ResponseTemplate<List<ProjectResponse>> mine(@RequestHeader("X-User-Id") Long userId) {
        return ResponseTemplate.success("Projects loaded", projectService.myProjects(userId));
    }

    @GetMapping("/{projectId}")
    public ResponseTemplate<ProjectResponse> findById(@PathVariable Long projectId) {
        return ResponseTemplate.success("Project loaded", projectService.findById(projectId));
    }

    @PutMapping("/{projectId}")
    public ResponseTemplate<ProjectResponse> update(@PathVariable Long projectId,
                                                    @RequestHeader("X-User-Id") Long userId,
                                                    @Valid @RequestBody UpdateProjectRequest request) {
        return ResponseTemplate.success("Project updated", projectService.update(projectId, request, userId));
    }

    @PostMapping("/{projectId}/members")
    public ResponseTemplate<ProjectMemberResponse> addMember(@PathVariable Long projectId,
                                                             @RequestHeader("X-User-Id") Long userId,
                                                             @Valid @RequestBody AddMemberRequest request) {
        return ResponseTemplate.success("Member added", projectService.addMember(projectId, request, userId));
    }

    @GetMapping("/{projectId}/members")
    public ResponseTemplate<List<ProjectMemberResponse>> members(@PathVariable Long projectId) {
        return ResponseTemplate.success("Members loaded", projectService.members(projectId));
    }

    @GetMapping("/{projectId}/permissions/{userId}")
    public ResponseTemplate<ProjectPermissionResponse> permissions(@PathVariable Long projectId,
                                                                   @PathVariable Long userId) {
        return ResponseTemplate.success("Permissions loaded", projectService.permissions(projectId, userId));
    }

    @GetMapping("/{projectId}/permissions/me")
    public ResponseTemplate<ProjectPermissionResponse> myPermissions(@PathVariable Long projectId,
                                                                     @RequestHeader("X-User-Id") Long userId) {
        return ResponseTemplate.success("Permissions loaded", projectService.permissions(projectId, userId));
    }

    @PostMapping("/{projectId}/invitations")
    public ResponseTemplate<ProjectInvitationResponse> inviteMember(@PathVariable Long projectId,
                                                                    @RequestHeader("X-User-Id") Long userId,
                                                                    @Valid @RequestBody InviteMemberRequest request) {
        return ResponseTemplate.success("Invitation sent", projectService.inviteMember(projectId, userId, request));
    }

    @GetMapping("/{projectId}/invitations")
    public ResponseTemplate<List<ProjectInvitationResponse>> invitations(@PathVariable Long projectId,
                                                                        @RequestHeader("X-User-Id") Long userId) {
        return ResponseTemplate.success("Invitations loaded", projectService.invitations(projectId, userId));
    }

    @PostMapping("/invitations/{token}/accept")
    public ResponseTemplate<ProjectMemberResponse> acceptInvitation(@PathVariable String token,
                                                                    @RequestHeader("X-User-Id") Long userId,
                                                                    @RequestHeader("X-User-Email") String userEmail) {
        return ResponseTemplate.success("Invitation accepted", projectService.acceptInvitation(token, userId, userEmail));
    }
}
