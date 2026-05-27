package com.kbase.project.repository;

import com.kbase.project.model.InvitationStatus;
import com.kbase.project.model.ProjectInvitation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectInvitationRepository extends JpaRepository<ProjectInvitation, Long> {
    Optional<ProjectInvitation> findByToken(String token);
    List<ProjectInvitation> findByProjectIdOrderByCreatedAtDesc(Long projectId);
    boolean existsByProjectIdAndInvitedEmailIgnoreCaseAndStatus(Long projectId, String invitedEmail, InvitationStatus status);
}
