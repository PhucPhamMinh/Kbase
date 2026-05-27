package com.kbase.project.service;

import com.kbase.project.dto.AddMemberRequest;
import com.kbase.project.dto.CreateProjectRequest;
import com.kbase.project.dto.InviteMemberRequest;
import com.kbase.project.dto.ProjectInvitationResponse;
import com.kbase.project.dto.ProjectMemberResponse;
import com.kbase.project.dto.ProjectPermissionResponse;
import com.kbase.project.dto.ProjectResponse;
import com.kbase.project.dto.UpdateProjectRequest;
import com.kbase.project.exception.BadRequestException;
import com.kbase.project.exception.ForbiddenException;
import com.kbase.project.exception.ResourceNotFoundException;
import com.kbase.project.model.InvitationStatus;
import com.kbase.project.model.MemberRole;
import com.kbase.project.model.Project;
import com.kbase.project.model.ProjectInvitation;
import com.kbase.project.model.ProjectMember;
import com.kbase.project.model.ProjectMemberId;
import com.kbase.project.repository.ProjectInvitationRepository;
import com.kbase.project.repository.ProjectMemberRepository;
import com.kbase.project.repository.ProjectRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository memberRepository;
    private final ProjectInvitationRepository invitationRepository;
    private final EmailService emailService;

    public ProjectService(ProjectRepository projectRepository,
                          ProjectMemberRepository memberRepository,
                          ProjectInvitationRepository invitationRepository,
                          EmailService emailService) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.invitationRepository = invitationRepository;
        this.emailService = emailService;
    }

    @Transactional
    public ProjectResponse create(CreateProjectRequest request, Long ownerId) {
        try {
            Project project = new Project();
            project.setProjectName(request.projectName());
            project.setDescription(request.description());
            project.setOwnerId(ownerId);
            Project saved = projectRepository.save(project);
            saveMember(saved.getProjectId(), ownerId, MemberRole.OWNER, true, true, true, true);
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
        requirePermission(projectId, userId, Permission.MODIFY);
        Project project = getProject(projectId);
        project.setProjectName(request.projectName());
        project.setDescription(request.description());
        if (request.active() != null) {
            project.setActive(request.active());
        }
        return toProjectResponse(projectRepository.save(project));
    }

    @Transactional
    public ProjectMemberResponse addMember(Long projectId, AddMemberRequest request, Long ownerId) {
        getProject(projectId);
        requireOwner(projectId, ownerId);
        if (memberRepository.existsById(new ProjectMemberId(projectId, request.userId()))) {
            throw new BadRequestException("User is already a project member");
        }
        boolean ownerRole = request.role() == MemberRole.OWNER;
        return toMemberResponse(saveMember(
                projectId,
                request.userId(),
                request.role(),
                ownerRole || valueOrFalse(request.canRead()),
                ownerRole || valueOrFalse(request.canAdd()),
                ownerRole || valueOrFalse(request.canModify()),
                ownerRole || valueOrFalse(request.canDelete())
        ));
    }

    public List<ProjectMemberResponse> members(Long projectId) {
        return memberRepository.findByIdProjectIdAndActiveTrue(projectId).stream().map(this::toMemberResponse).toList();
    }

    public ProjectPermissionResponse permissions(Long projectId, Long userId) {
        Project project = getProject(projectId);
        if (project.getOwnerId().equals(userId)) {
            return new ProjectPermissionResponse(projectId, userId, true, true, true, true, true);
        }
        ProjectMember member = memberRepository.findById(new ProjectMemberId(projectId, userId))
                .orElseThrow(() -> new ForbiddenException("You are not a member of this project"));
        if (!Boolean.TRUE.equals(member.getActive())) {
            throw new ForbiddenException("Your project membership is inactive");
        }
        return new ProjectPermissionResponse(
                projectId,
                userId,
                false,
                member.getCanRead(),
                member.getCanAdd(),
                member.getCanModify(),
                member.getCanDelete()
        );
    }

    @Transactional
    public ProjectInvitationResponse inviteMember(Long projectId, Long ownerId, InviteMemberRequest request) {
        Project project = getProject(projectId);
        requireOwner(projectId, ownerId);
        String email = request.email().trim().toLowerCase();
        if (email.isBlank()) {
            throw new BadRequestException("Email is required");
        }
        if (invitationRepository.existsByProjectIdAndInvitedEmailIgnoreCaseAndStatus(projectId, email, InvitationStatus.PENDING)) {
            throw new BadRequestException("This email already has a pending invitation");
        }

        ProjectInvitation invitation = new ProjectInvitation();
        invitation.setProjectId(projectId);
        invitation.setInvitedEmail(email);
        invitation.setInvitedBy(ownerId);
        invitation.setToken(UUID.randomUUID().toString());
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setCanRead(request.canRead());
        invitation.setCanAdd(request.canAdd());
        invitation.setCanModify(request.canModify());
        invitation.setCanDelete(request.canDelete());
        invitation.setExpiresAt(LocalDateTime.now().plusDays(7));

        ProjectInvitation saved = invitationRepository.save(invitation);
        emailService.sendProjectInvitation(project, saved);
        return toInvitationResponse(saved);
    }

    public List<ProjectInvitationResponse> invitations(Long projectId, Long ownerId) {
        requireOwner(projectId, ownerId);
        return invitationRepository.findByProjectIdOrderByCreatedAtDesc(projectId)
                .stream()
                .map(this::toInvitationResponse)
                .toList();
    }

    @Transactional
    public ProjectMemberResponse acceptInvitation(String token, Long userId, String userEmail) {
        ProjectInvitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new BadRequestException("Invitation is no longer pending");
        }
        if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
            invitation.setStatus(InvitationStatus.EXPIRED);
            invitationRepository.save(invitation);
            throw new BadRequestException("Invitation has expired");
        }
        if (userEmail == null || !invitation.getInvitedEmail().equalsIgnoreCase(userEmail.trim())) {
            throw new ForbiddenException("This invitation belongs to another email address");
        }
        if (memberRepository.existsById(new ProjectMemberId(invitation.getProjectId(), userId))) {
            throw new BadRequestException("You are already a member of this project");
        }

        ProjectMember member = saveMember(
                invitation.getProjectId(),
                userId,
                MemberRole.MEMBER,
                invitation.getCanRead(),
                invitation.getCanAdd(),
                invitation.getCanModify(),
                invitation.getCanDelete()
        );
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setAcceptedAt(LocalDateTime.now());
        invitationRepository.save(invitation);
        return toMemberResponse(member);
    }

    private Project getProject(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    private ProjectMember saveMember(Long projectId, Long userId, MemberRole role, Boolean canRead, Boolean canAdd, Boolean canModify, Boolean canDelete) {
        ProjectMember member = new ProjectMember();
        member.setId(new ProjectMemberId(projectId, userId));
        member.setRole(role);
        member.setCanRead(canRead);
        member.setCanAdd(canAdd);
        member.setCanModify(canModify);
        member.setCanDelete(canDelete);
        member.setActive(true);
        return memberRepository.save(member);
    }

    private void requireOwner(Long projectId, Long userId) {
        Project project = getProject(projectId);
        if (!project.getOwnerId().equals(userId)) {
            throw new ForbiddenException("Only the project owner can perform this action");
        }
    }

    private void requirePermission(Long projectId, Long userId, Permission permission) {
        Project project = getProject(projectId);
        if (project.getOwnerId().equals(userId)) {
            return;
        }
        ProjectMember member = memberRepository.findById(new ProjectMemberId(projectId, userId))
                .orElseThrow(() -> new ForbiddenException("You are not a member of this project"));
        if (!Boolean.TRUE.equals(member.getActive())) {
            throw new ForbiddenException("Your project membership is inactive");
        }
        boolean allowed = switch (permission) {
            case READ -> Boolean.TRUE.equals(member.getCanRead());
            case ADD -> Boolean.TRUE.equals(member.getCanAdd());
            case MODIFY -> Boolean.TRUE.equals(member.getCanModify());
            case DELETE -> Boolean.TRUE.equals(member.getCanDelete());
        };
        if (!allowed) {
            throw new ForbiddenException("You do not have permission to perform this action");
        }
    }

    private Boolean valueOrFalse(Boolean value) {
        return Boolean.TRUE.equals(value);
    }

    private ProjectResponse toProjectResponse(Project project) {
        return new ProjectResponse(project.getProjectId(), project.getProjectName(), project.getDescription(), project.getOwnerId(), project.getActive());
    }

    private ProjectMemberResponse toMemberResponse(ProjectMember member) {
        return new ProjectMemberResponse(
                member.getId().getProjectId(),
                member.getId().getUserId(),
                member.getRole(),
                member.getCanRead(),
                member.getCanAdd(),
                member.getCanModify(),
                member.getCanDelete(),
                member.getActive()
        );
    }

    private ProjectInvitationResponse toInvitationResponse(ProjectInvitation invitation) {
        return new ProjectInvitationResponse(
                invitation.getInvitationId(),
                invitation.getProjectId(),
                invitation.getInvitedEmail(),
                invitation.getInvitedBy(),
                invitation.getStatus(),
                invitation.getCanRead(),
                invitation.getCanAdd(),
                invitation.getCanModify(),
                invitation.getCanDelete(),
                invitation.getExpiresAt(),
                invitation.getAcceptedAt()
        );
    }

    private enum Permission {
        READ,
        ADD,
        MODIFY,
        DELETE
    }
}
