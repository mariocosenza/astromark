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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
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
        // Validate the filename
        String originalFilename = Objects.requireNonNull(multipartFile.getOriginalFilename());
        validateFilename(originalFilename);

        // Convert multipart file to a File object
        File file = new File(originalFilename);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(multipartFile.getBytes());
        }

        // Generate a unique file name
        String fileName = generateFileName(multipartFile);

        // Upload the file
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType("plain/" + FilenameUtils.getExtension(multipartFile.getOriginalFilename()))
                .metadata(Map.of("Title", "File Upload - " + fileName))
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

        // Delete the temporary file
        //noinspection ResultOfMethodCallIgnored
        file.delete();

        return fileName;
    }

    @Override
    public boolean delete(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);

        File file = Paths.get(fileName).toFile();
        if (file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
            return true;
        }
        return false;
    }


    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }

    private void validateFilename(String filename) {
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            throw new IllegalArgumentException("Invalid filename");
        }
    }
}
