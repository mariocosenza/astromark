package it.astromark.chat.service;

import it.astromark.chat.dto.MessageResponse;
import it.astromark.chat.entity.Message;
import it.astromark.user.commons.model.SchoolUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Service interface for managing message-related operations.
 * Provides methods for retrieving message details, adding attachments, and creating messages.
 */
public interface MessageService {

    /**
     * Retrieves the sender of a specific message.
     *
     * @param message the `Message` object
     * @return a `SchoolUser` object representing the sender of the message
     * Pre-condition: The `message` must not be null and must have a valid sender associated with it.
     * Post-condition: Returns the `SchoolUser` who sent the specified message.
     */
    SchoolUser getSender(Message message);

    /**
     * Adds an attachment to a specified entity.
     *
     * @param uuid the UUID of the entity to associate with the attachment
     * @param multipartFile the file to be uploaded as an attachment
     * @return a string representing the file path or identifier of the uploaded attachment
     * @throws IOException if there is an error during file upload
     * Pre-condition: The `uuid` and `multipartFile` must not be null. The `multipartFile` must be a valid, non-empty file.
     * Post-condition: The file is uploaded and associated with the specified entity. Returns the file path or identifier.
     */
    String addAttachment(UUID uuid, MultipartFile multipartFile) throws IOException;

    /**
     * Creates a new message in a specific chat.
     *
     * @param message the message content
     * @param chatId the UUID of the chat
     * @param isHomework true if the message is related to homework, false otherwise
     * @return a `MessageResponse` object representing the created message
     * Pre-condition: The `message` must not be null or empty. The `chatId` must not be null and must refer to an existing chat.
     * Post-condition: A new message is created and added to the specified chat. Returns a response object with the message details.
     */
    MessageResponse create(String message, UUID chatId, boolean isHomework);

}
