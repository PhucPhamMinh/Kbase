package com.kbase.document.repository;

import com.kbase.document.model.Document;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByProjectIdAndDeletedFalseOrderByCreatedAtDesc(Long projectId);
    List<Document> findByProjectIdAndDeletedTrueOrderByDeletedAtDesc(Long projectId);
    List<Document> findByDeletedTrueAndDeleteAfterBefore(LocalDateTime now);
}
