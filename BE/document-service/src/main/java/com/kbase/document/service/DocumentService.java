package com.kbase.document.service;

import com.kbase.document.dto.DocumentReadUrlResponse;
import com.kbase.document.dto.DocumentResponse;
import com.kbase.document.dto.UpdateDocumentRequest;
import com.kbase.document.exception.ResourceNotFoundException;
import com.kbase.document.model.Document;
import com.kbase.document.repository.DocumentRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {
    private static final int SOFT_DELETE_RETENTION_DAYS = 30;

    private final DocumentRepository documentRepository;
    private final S3StorageService s3StorageService;
    private final ProjectPermissionClient projectPermissionClient;

    public DocumentService(DocumentRepository documentRepository,
                           S3StorageService s3StorageService,
                           ProjectPermissionClient projectPermissionClient) {
        this.documentRepository = documentRepository;
        this.s3StorageService = s3StorageService;
        this.projectPermissionClient = projectPermissionClient;
    }

    @Transactional
    public DocumentResponse upload(Long projectId, Long uploadedBy, String title, String description, MultipartFile file) {
        projectPermissionClient.requireAdd(projectId, uploadedBy);
        String storagePath = s3StorageService.upload(projectId, file);
        String fileName = file.getOriginalFilename() == null ? "document" : file.getOriginalFilename();
        Document document = new Document();
        document.setProjectId(projectId);
        document.setUploadedBy(uploadedBy);
        document.setTitle(title);
        document.setFileName(fileName);
        document.setFileType(resolveFileType(fileName));
        document.setFileSize(file.getSize());
        document.setStoragePath(storagePath);
        document.setMimeType(file.getContentType() == null ? "application/octet-stream" : file.getContentType());
        document.setDescription(description);
        document.setDeleted(false);
        return toResponse(documentRepository.save(document));
    }

    public List<DocumentResponse> findByProject(Long projectId, Long userId) {
        projectPermissionClient.requireRead(projectId, userId);
        return documentRepository.findByProjectIdAndDeletedFalseOrderByCreatedAtDesc(projectId).stream().map(this::toResponse).toList();
    }

    public List<DocumentResponse> findDeletedByProject(Long projectId, Long userId) {
        projectPermissionClient.requireDelete(projectId, userId);
        return documentRepository.findByProjectIdAndDeletedTrueOrderByDeletedAtDesc(projectId).stream().map(this::toResponse).toList();
    }

    public DocumentResponse findById(Long documentId, Long userId) {
        Document document = getDocument(documentId);
        projectPermissionClient.requireRead(document.getProjectId(), userId);
        rejectDeleted(document);
        return toResponse(document);
    }

    @Transactional
    public DocumentResponse update(Long documentId, Long userId, UpdateDocumentRequest request) {
        Document document = getDocument(documentId);
        projectPermissionClient.requireModify(document.getProjectId(), userId);
        rejectDeleted(document);
        document.setTitle(request.title());
        document.setDescription(request.description());
        return toResponse(documentRepository.save(document));
    }

    @Transactional
    public DocumentResponse delete(Long documentId, Long userId) {
        Document document = getDocument(documentId);
        projectPermissionClient.requireDelete(document.getProjectId(), userId);
        if (Boolean.TRUE.equals(document.getDeleted())) {
            return toResponse(document);
        }
        LocalDateTime now = LocalDateTime.now();
        document.setDeleted(true);
        document.setDeletedAt(now);
        document.setDeletedBy(userId);
        document.setDeleteAfter(now.plusDays(SOFT_DELETE_RETENTION_DAYS));
        return toResponse(documentRepository.save(document));
    }

    @Transactional
    public DocumentResponse restore(Long documentId, Long userId) {
        Document document = getDocument(documentId);
        projectPermissionClient.requireDelete(document.getProjectId(), userId);
        if (!Boolean.TRUE.equals(document.getDeleted())) {
            return toResponse(document);
        }
        if (document.getDeleteAfter() != null && document.getDeleteAfter().isBefore(LocalDateTime.now())) {
            throw new ResourceNotFoundException("Document is past its restore window");
        }
        document.setDeleted(false);
        document.setDeletedAt(null);
        document.setDeletedBy(null);
        document.setDeleteAfter(null);
        return toResponse(documentRepository.save(document));
    }

    @Transactional
    public int purgeExpiredSoftDeletedDocuments() {
        List<Document> expiredDocuments = documentRepository.findByDeletedTrueAndDeleteAfterBefore(LocalDateTime.now());
        expiredDocuments.forEach(document -> {
            s3StorageService.delete(document.getStoragePath());
            documentRepository.delete(document);
        });
        return expiredDocuments.size();
    }

    public DocumentReadUrlResponse createReadUrl(Long documentId, Long userId) {
        Document document = getDocument(documentId);
        projectPermissionClient.requireRead(document.getProjectId(), userId);
        rejectDeleted(document);
        S3StorageService.ReadUrl readUrl = s3StorageService.createReadUrl(
                document.getStoragePath(),
                document.getFileName(),
                document.getMimeType()
        );
        return new DocumentReadUrlResponse(document.getDocumentId(), readUrl.url(), readUrl.expiresAt());
    }

    private Document getDocument(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
    }

    private void rejectDeleted(Document document) {
        if (Boolean.TRUE.equals(document.getDeleted())) {
            throw new ResourceNotFoundException("Document has been deleted");
        }
    }

    private String resolveFileType(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "unknown";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }

    private DocumentResponse toResponse(Document document) {
        return new DocumentResponse(
                document.getDocumentId(),
                document.getProjectId(),
                document.getUploadedBy(),
                document.getTitle(),
                document.getFileName(),
                document.getFileType(),
                document.getFileSize(),
                document.getStoragePath(),
                document.getMimeType(),
                document.getDescription(),
                document.getDeleted(),
                document.getDeletedAt() == null ? null : document.getDeletedAt().toString(),
                document.getDeleteAfter() == null ? null : document.getDeleteAfter().toString()
        );
    }
}
