package com.kbase.document.service;

import com.kbase.document.dto.DocumentReadUrlResponse;
import com.kbase.document.dto.DocumentResponse;
import com.kbase.document.dto.UpdateDocumentRequest;
import com.kbase.document.exception.ResourceNotFoundException;
import com.kbase.document.model.Document;
import com.kbase.document.repository.DocumentRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {
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
        return toResponse(documentRepository.save(document));
    }

    public List<DocumentResponse> findByProject(Long projectId, Long userId) {
        projectPermissionClient.requireRead(projectId, userId);
        return documentRepository.findByProjectIdOrderByCreatedAtDesc(projectId).stream().map(this::toResponse).toList();
    }

    public DocumentResponse findById(Long documentId, Long userId) {
        Document document = getDocument(documentId);
        projectPermissionClient.requireRead(document.getProjectId(), userId);
        return toResponse(document);
    }

    @Transactional
    public DocumentResponse update(Long documentId, Long userId, UpdateDocumentRequest request) {
        Document document = getDocument(documentId);
        projectPermissionClient.requireModify(document.getProjectId(), userId);
        document.setTitle(request.title());
        document.setDescription(request.description());
        return toResponse(documentRepository.save(document));
    }

    @Transactional
    public void delete(Long documentId, Long userId) {
        Document document = getDocument(documentId);
        projectPermissionClient.requireDelete(document.getProjectId(), userId);
        s3StorageService.delete(document.getStoragePath());
        documentRepository.delete(document);
    }

    public DocumentReadUrlResponse createReadUrl(Long documentId, Long userId) {
        Document document = getDocument(documentId);
        projectPermissionClient.requireRead(document.getProjectId(), userId);
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
                document.getDescription()
        );
    }
}
