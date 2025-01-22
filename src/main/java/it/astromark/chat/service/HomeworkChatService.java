package it.astromark.chat.service;

import it.astromark.chat.dto.HomeworkChatResponse;
import it.astromark.chat.dto.MessageResponse;
import it.astromark.user.teacher.entity.Teacher;

import java.util.List;
import java.util.UUID;

public interface HomeworkChatService {
    /**
     * Retrieves a homework chat along with its messages using a socket connection.
     *
     * @param chatId the UUID of the chat
     * @return a `HomeworkChatResponse` object containing the chat details and messages
     * Pre-condition: The `chatId` must not be null. The chat associated with the `chatId` must exist.
     * Post-condition: Returns the chat details along with its messages.
     */
    HomeworkChatResponse getChatWithMessagesSocket(UUID chatId);

    /**
     * Sends a message to a specific homework chat.
     *
     * @param chatId the UUID of the chat
     * @param text the message text to be sent
     * @return the UUID of the sent message
     * Pre-condition: The `chatId` and `text` must not be null or empty. The chat associated with the `chatId` must exist.
     * Post-condition: The message is sent and its UUID is returned.
     */
    UUID sendMessage(UUID chatId, String text);

    /**
     * Finds the teacher associated with a specific chat.
     *
     * @param chatId the UUID of the chat
     * @return the `Teacher` object associated with the chat
     * Pre-condition: The `chatId` must not be null. The chat associated with the `chatId` must exist and have a teacher assigned.
     * Post-condition: Returns the teacher associated with the chat.
     */
    Teacher findTeacher(UUID chatId);

    /**
     * Adds a new chat for a specific homework.
     *
     * @param homeworkId the ID of the homework
     * Pre-condition: The `homeworkId` must not be null. The homework associated with the `homeworkId` must exist.
     * Post-condition: A new chat is created and associated with the specified homework.
     */
    void addChat(Integer homeworkId);

    /**
     * Retrieves the list of messages from a specific chat.
     *
     * @param chatId the UUID of the chat
     * @return a list of `MessageResponse` objects representing the messages
     * Pre-condition: The `chatId` must not be null. The chat associated with the `chatId` must exist.
     * Post-condition: Returns the list of messages for the specified chat.
     */
    List<MessageResponse> getMessageList(UUID chatId);

    /**
     * Checks if there is an uncompleted homework chat for a specific homework.
     *
     * @param homeworkId the ID of the homework
     * @return the UUID of the uncompleted homework chat, or null if none exists
     * Pre-condition: The `homeworkId` must not be null. The homework associated with the `homeworkId` must exist.
     * Post-condition: Returns the UUID of the uncompleted homework chat if one exists, otherwise null.
     */
    UUID hasUncompletedHomeworkChat(Integer homeworkId);

}
