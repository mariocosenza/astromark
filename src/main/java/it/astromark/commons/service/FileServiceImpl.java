package it.astromark.commons.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final S3Client s3Client;
    @Value("${aws.bucket.name}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        // 1. Check file size (16MB = 16 * 1024 * 1024 bytes)
        long maxSize =((long) 16) * 1024 * 1024;
        if (multipartFile.getSize() > maxSize) {
            throw new IllegalArgumentException("File size exceeds maximum limit of 16MB");
        }

        // 2. Get a safe, sanitized file name (removing any path components)
        String originalFilename = Objects.requireNonNull(multipartFile.getOriginalFilename(),
                "File name cannot be null");
        String safeFilename = FilenameUtils.getName(originalFilename);

        // 3. Generate a unique file name (e.g., using timestamp)
        String fileName = generateFileName(safeFilename);

        // 4. Prepare metadata map with content type detection
        String contentType = multipartFile.getContentType();
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", contentType);
        metadata.put("Original-Filename", safeFilename);
        metadata.put("Upload-Date", LocalDateTime.now().toString());
        metadata.put("File-Size", String.valueOf(multipartFile.getSize()));

        // 5. Prepare the PutObjectRequest
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .metadata(metadata)
                .build();

        // 6. Upload the file directly from multipart bytes
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));
        } catch (SdkException e) {
            log.error("Error uploading file to S3: {}", e.getMessage());
            throw new IOException("Failed to upload file to S3", e);
        }

        // 7. Retrieve the file URL (assuming the object/bucket is publicly accessible)
        String fileUrl = s3Client.utilities()
                .getUrl(GetUrlRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build())
                .toExternalForm();

        log.info("File successfully uploaded: {}", fileUrl);
        return safeFilename;
    }

    @Override
    public boolean delete(String fileName) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            log.info("File successfully deleted: {}", fileName);
            return true;
        } catch (S3Exception e) {
            log.error("Error deleting file {} from S3", fileName, e);
            return false;
        }
    }

    /**
     * Generates a timestamp-based file name.
     */
    private String generateFileName(String safeFilename) {
        return new Date().getTime() + "-" + safeFilename.replace(" ", "_");
    }
}
