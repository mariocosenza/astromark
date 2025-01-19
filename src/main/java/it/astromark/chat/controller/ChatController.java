package it.astromark.chat.controller;

import it.astromark.chat.service.MessageService;
import it.astromark.commons.dto.APIResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/chats")
public class ChatController {

    private final MessageService messageService;

    public ChatController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/upload/{messageId}")
    public ResponseEntity<APIResponse> uploadFile(@RequestParam("file") MultipartFile multipartFile, @PathVariable @NotNull UUID messageId) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new FileUploadException("File is empty. Cannot save an empty file");
        }

        boolean isValidFile = isValidFile(multipartFile);
        List<String> allowedFileExtensions = List.of("pdf", "txt", "epub", "csv", "png", "jpg", "jpeg", "srt");

        if (isValidFile && allowedFileExtensions.contains(FilenameUtils.getExtension(multipartFile.getOriginalFilename()))) {
            String fileName =  messageService.addAttachment(messageId, multipartFile);

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
