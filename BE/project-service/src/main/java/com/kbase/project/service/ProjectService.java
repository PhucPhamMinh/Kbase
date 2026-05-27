package com.kbase.project.service;

import com.kbase.project.dto.AddMemberRequest;
import com.kbase.project.dto.CreateProjectRequest;
import com.kbase.project.dto.ProjectMemberResponse;
import com.kbase.project.dto.ProjectResponse;
import com.kbase.project.dto.UpdateProjectRequest;
import com.kbase.project.exception.ResourceNotFoundException;
import com.kbase.project.model.MemberRole;
import com.kbase.project.model.Project;
import com.kbase.project.model.ProjectMember;
import com.kbase.project.model.ProjectMemberId;
import com.kbase.project.repository.ProjectMemberRepository;
import com.kbase.project.repository.ProjectRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository memberRepository;

    public ProjectService(ProjectRepository projectRepository, ProjectMemberRepository memberRepository) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public ProjectResponse create(CreateProjectRequest request, Long ownerId) {
        try {
            Project project = new Project();
            project.setProjectName(request.projectName());
            project.setDescription(request.description());
            project.setOwnerId(ownerId);
            Project saved = projectRepository.save(project);
            saveMember(saved.getProjectId(), ownerId, MemberRole.OWNER);
            return toProjectResponse(saved);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to create project", ex);
        }
    }

    public List<ProjectResponse> myProjects(Long userId) {
        List<Long> memberProjectIds = memberRepository.findByIdUserIdAndActiveTrue(userId).stream()
                .map(member -> member.getId().getProjectId())
                .toList();
        return projectRepository.findAllById(memberProjectIds).stream().map(this::toProjectResponse).toList();
    }

    public ProjectResponse findById(Long projectId) {
        return toProjectResponse(getProject(projectId));
    }

    @Transactional
    public ProjectResponse update(Long projectId, UpdateProjectRequest request, Long userId) {
        Project project = getProject(projectId);
        project.setProjectName(request.projectName());
        project.setDescription(request.description());
        if (request.active() != null) {
            project.setActive(request.active());
        }
        return toProjectResponse(projectRepository.save(project));
    }

    @Transactional
    public ProjectMemberResponse addMember(Long projectId, AddMemberRequest request) {
        getProject(projectId);
        return toMemberResponse(saveMember(projectId, request.userId(), request.role()));
    }

    public List<ProjectMemberResponse> members(Long projectId) {
        return memberRepository.findByIdProjectIdAndActiveTrue(projectId).stream().map(this::toMemberResponse).toList();
    }

    private Project getProject(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    private ProjectMember saveMember(Long projectId, Long userId, MemberRole role) {
        ProjectMember member = new ProjectMember();
        member.setId(new ProjectMemberId(projectId, userId));
        member.setRole(role);
        member.setActive(true);
        return memberRepository.save(member);
    }

    private ProjectResponse toProjectResponse(Project project) {
        return new ProjectResponse(project.getProjectId(), project.getProjectName(), project.getDescription(), project.getOwnerId(), project.getActive());
    }

    private ProjectMemberResponse toMemberResponse(ProjectMember member) {
        return new ProjectMemberResponse(member.getId().getProjectId(), member.getId().getUserId(), member.getRole(), member.getActive());
    }
}
