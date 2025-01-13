package it.astromark.commons.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    private final S3Client s3Client;

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        // 1. Get a safe, sanitized file name (removing any path components)
        String originalFilename = Objects.requireNonNull(multipartFile.getOriginalFilename(),
                "File name cannot be null");
        String safeFilename = FilenameUtils.getName(originalFilename);

        // 2. Generate a unique file name (e.g., using timestamp)
        String fileName = generateFileName(safeFilename);

        // 3. Prepare the PutObjectRequest
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType("plain/" + FilenameUtils.getExtension(safeFilename))
                .metadata(Map.of("Title", "File Upload - " + fileName))
                .build();

        // 4. Upload the file directly from multipart bytes
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));

        // 5. Retrieve the file URL (assuming the object/bucket is publicly accessible)
        String fileUrl = s3Client.utilities()
                .getUrl(GetUrlRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build())
                .toExternalForm();

        log.info("File successfully uploaded: {}", fileUrl);
        return fileUrl;
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
