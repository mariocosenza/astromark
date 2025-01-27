package it.astromark.commons.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service interface for managing file-related operations.
 * Provides methods for handling file storage, retrieval, and management.
 */
public interface FileService {
    /**
     * Uploads a file and returns its identifier or path.
     *
     * @param multipartFile the file to be uploaded
     * @return a string representing the file's identifier or storage path
     * @throws IOException if an error occurs during file upload
     *                     Pre-condition: The `multipartFile` must not be null and must contain valid file data.
     *                     Post-condition: The file is uploaded, and its identifier or path is returned.
     */
    String uploadFile(MultipartFile multipartFile) throws IOException;

    /**
     * Deletes a file by its name.
     *
     * @param fileName the name of the file to be deleted
     * @return true if the file is successfully deleted, false otherwise
     * Pre-condition: The `fileName` must not be null or empty and must correspond to an existing file.
     * Post-condition: The file is deleted if it exists, and the method returns true.
     */
    boolean delete(String fileName);

}
