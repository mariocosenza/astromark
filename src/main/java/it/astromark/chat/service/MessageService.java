package it.astromark.chat.service;

import it.astromark.chat.dto.MessageResponse;
import it.astromark.chat.entity.Message;

import it.astromark.user.commons.model.SchoolUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface MessageService  {

    SchoolUser getSender(Message message);
    String addAttachment(UUID uuid, MultipartFile multipartFile) throws IOException;
    MessageResponse create(String message, UUID chatId, boolean isHomework);
}
