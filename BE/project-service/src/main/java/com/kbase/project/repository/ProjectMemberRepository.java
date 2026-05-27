package com.kbase.project.repository;

import com.kbase.project.model.ProjectMember;
import com.kbase.project.model.ProjectMemberId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    List<ProjectMember> findByIdUserIdAndActiveTrue(Long userId);
    List<ProjectMember> findByIdProjectIdAndActiveTrue(Long projectId);
}
