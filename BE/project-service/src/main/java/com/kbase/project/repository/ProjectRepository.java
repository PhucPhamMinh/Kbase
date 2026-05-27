package com.kbase.project.repository;

import com.kbase.project.model.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwnerIdAndActiveTrue(Long ownerId);
}
