package com.kbase.document.service;

import com.kbase.document.exception.BadRequestException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
public class S3StorageService {
    public static final long MAX_FILE_SIZE_BYTES = 10L * 1024L * 1024L;
    private static final Duration READ_URL_TTL = Duration.ofMinutes(10);

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;

    public S3StorageService(S3Client s3Client, S3Presigner s3Presigner, @Value("${aws.s3.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
    }

    public String upload(Long projectId, MultipartFile file) {
        validateFile(file);
        String originalName = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
        String key = "projects/%d/%s-%s".formatted(projectId, UUID.randomUUID(), originalName.replaceAll("[^A-Za-z0-9._-]", "_"));
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();
            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return key;
        } catch (IOException ex) {
            throw new BadRequestException("Unable to read uploaded file");
        } catch (Exception ex) {
            throw new BadRequestException("Unable to upload file to S3");
        }
    }

    public ReadUrl createReadUrl(String storagePath, String fileName, String mimeType) {
        String safeFileName = fileName == null ? "document" : fileName.replaceAll("[\\r\\n\"]", "_");
        Instant expiresAt = Instant.now().plus(READ_URL_TTL);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(storagePath)
                .responseContentType(mimeType)
                .responseContentDisposition("inline; filename=\"" + safeFileName + "\"")
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(READ_URL_TTL)
                .getObjectRequest(getObjectRequest)
                .build();
        String url = s3Presigner.presignGetObject(presignRequest).url().toString();
        return new ReadUrl(url, expiresAt);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required");
        }
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new BadRequestException("File size must not exceed 10MB");
        }
    }

    public record ReadUrl(String url, Instant expiresAt) {
    }
}
