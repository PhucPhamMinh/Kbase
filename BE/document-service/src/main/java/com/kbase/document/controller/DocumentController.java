package com.kbase.document.controller;

import com.kbase.document.common.ResponseTemplate;
import com.kbase.document.dto.DocumentReadUrlResponse;
import com.kbase.document.dto.DocumentResponse;
import com.kbase.document.dto.UpdateDocumentRequest;
import com.kbase.document.exception.BadRequestException;
import com.kbase.document.service.DocumentService;
import org.springframework.beans.factory.annotation.Value;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;
    private final String lambdaPurgeSecret;

    public DocumentController(DocumentService documentService,
                              @Value("${lambda.purge.secret}") String lambdaPurgeSecret) {
        this.documentService = documentService;
        this.lambdaPurgeSecret = lambdaPurgeSecret;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseTemplate<DocumentResponse> upload(@RequestHeader("X-User-Id") Long userId,
                                                     @RequestParam @NotNull Long projectId,
                                                     @RequestParam @NotBlank @Size(max = 255) String title,
                                                     @RequestParam(required = false) @Size(max = 5000) String description,
                                                     @RequestParam MultipartFile file) {
        if (file.getSize() > 10L * 1024L * 1024L) {
            throw new BadRequestException("File size must not exceed 10MB");
        }
        return ResponseTemplate.success("Document uploaded", documentService.upload(projectId, userId, title, description, file));
    }

    @GetMapping
    public ResponseTemplate<List<DocumentResponse>> byProject(@RequestHeader("X-User-Id") Long userId,
                                                              @RequestParam @NotNull Long projectId) {
        return ResponseTemplate.success("Documents loaded", documentService.findByProject(projectId, userId));
    }

    @GetMapping("/deleted")
    public ResponseTemplate<List<DocumentResponse>> deletedByProject(@RequestHeader("X-User-Id") Long userId,
                                                                     @RequestParam @NotNull Long projectId) {
        return ResponseTemplate.success("Deleted documents loaded", documentService.findDeletedByProject(projectId, userId));
    }

    @GetMapping("/{documentId}")
    public ResponseTemplate<DocumentResponse> findById(@RequestHeader("X-User-Id") Long userId,
                                                       @PathVariable Long documentId) {
        return ResponseTemplate.success("Document loaded", documentService.findById(documentId, userId));
    }

    @PutMapping("/{documentId}")
    public ResponseTemplate<DocumentResponse> update(@RequestHeader("X-User-Id") Long userId,
                                                     @PathVariable Long documentId,
                                                     @Valid @RequestBody UpdateDocumentRequest request) {
        return ResponseTemplate.success("Document updated", documentService.update(documentId, userId, request));
    }

    @DeleteMapping("/{documentId}")
    public ResponseTemplate<DocumentResponse> delete(@RequestHeader("X-User-Id") Long userId,
                                                     @PathVariable Long documentId) {
        return ResponseTemplate.success("Document moved to trash", documentService.delete(documentId, userId));
    }

    @PostMapping("/{documentId}/restore")
    public ResponseTemplate<DocumentResponse> restore(@RequestHeader("X-User-Id") Long userId,
                                                      @PathVariable Long documentId) {
        return ResponseTemplate.success("Document restored", documentService.restore(documentId, userId));
    }

    @GetMapping("/{documentId}/read-url")
    public ResponseTemplate<DocumentReadUrlResponse> readUrl(@RequestHeader("X-User-Id") Long userId,
                                                             @PathVariable Long documentId) {
        return ResponseTemplate.success("Document read URL created", documentService.createReadUrl(documentId, userId));
    }

    @DeleteMapping("/purge-expired")
    public ResponseTemplate<Integer> purgeExpired(@RequestHeader("X-Lambda-Secret") String lambdaSecret) {
        if (!lambdaPurgeSecret.equals(lambdaSecret)) {
            throw new BadRequestException("Invalid purge secret");
        }
        return ResponseTemplate.success("Expired deleted documents purged", documentService.purgeExpiredSoftDeletedDocuments());
    }
}
