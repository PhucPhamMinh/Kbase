package com.kbase.document.repository;

import com.kbase.document.model.Document;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByProjectIdOrderByCreatedAtDesc(Long projectId);
}
