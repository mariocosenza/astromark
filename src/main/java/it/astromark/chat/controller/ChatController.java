package it.astromark.chat.controller;

import it.astromark.commons.dto.APIResponse;
import it.astromark.commons.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
public class ChatController {

    private final FileService fileService;

    public ChatController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<APIResponse> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new FileUploadException("File is empty. Cannot save an empty file");
        }

        boolean isValidFile = isValidFile(multipartFile);
        List<String> allowedFileExtensions = List.of("pdf", "txt", "epub", "csv", "png", "jpg", "jpeg", "srt");

        if (isValidFile && allowedFileExtensions.contains(FilenameUtils.getExtension(multipartFile.getOriginalFilename()))) {
            String fileName = fileService.uploadFile(multipartFile);

            APIResponse apiResponse = new APIResponse(
                    "File uploaded successfully. File unique name => " + fileName,
                    true,
                    200,
                    null
            );
            return ResponseEntity.ok(apiResponse);
        } else {
            APIResponse apiResponse = new APIResponse(
                    "Invalid File. File extension or File name is not supported",
                    false,
                    400,
                    null
            );
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    private boolean isValidFile(MultipartFile multipartFile){
        log.info("Empty Status ==> {}", multipartFile.isEmpty());
        if (Objects.isNull(multipartFile.getOriginalFilename())){
            return false;
        }
        return !multipartFile.getOriginalFilename().trim().isEmpty();
    }

}
