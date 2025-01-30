package it.astromark.chat.service;

import it.astromark.chat.dto.HomeworkChatResponse;
import it.astromark.chat.dto.MessageResponse;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing homework chat-related operations.
 * Provides methods for retrieving, sending messages, and managing homework chats.
 */
public interface HomeworkChatService {
    /**
     * Retrieves a homework chat along with its messages using a socket connection.
     *
     * @param chatId the UUID of the chat
     * @return a `HomeworkChatResponse` object containing the chat details and messages
     * Pre-condition: The `chatId` must not be null. The chat associated with the `chatId` must exist.
     * Post-condition: Returns the chat details along with its messages.
     */
    HomeworkChatResponse getChatWithMessagesSocket(@NotNull UUID chatId);

    /**
     * Sends a message to a specific homework chat.
     *
     * @param chatId the UUID of the chat
     * @param text   the message text to be sent
     * @return the UUID of the sent message
     * Pre-condition: The `chatId` and `text` must not be null or empty. The chat associated with the `chatId` must exist.
     * Post-condition: The message is sent and its UUID is returned.
     */
    UUID sendMessage(@NotNull UUID chatId, @NotEmpty String text);

    /**
     * Finds the teacher associated with a specific chat.
     *
     * @param chatId the UUID of the chat
     * @return the `Teacher` object associated with the chat
     * Pre-condition: The `chatId` must not be null. The chat associated with the `chatId` must exist and have a teacher assigned.
     * Post-condition: Returns the teacher associated with the chat.
     */
    Teacher findTeacher(@NotNull UUID chatId);

    /**
     * Adds a new chat for a specific homework.
     *
     * @param homeworkId the ID of the homework
     *                   Pre-condition: The `homeworkId` must not be null. The homework associated with the `homeworkId` must exist.
     *                   Post-condition: A new chat is created and associated with the specified homework.
     */
    void addChat(@NotNull Integer homeworkId);

    /**
     * Retrieves the list of messages from a specific chat.
     *
     * @param chatId the UUID of the chat
     * @return a list of `MessageResponse` objects representing the messages
     * Pre-condition: The `chatId` must not be null. The chat associated with the `chatId` must exist.
     * Post-condition: Returns the list of messages for the specified chat.
     */
    List<MessageResponse> getMessageList(@NotNull UUID chatId);

    /**
     * Checks if there is an uncompleted homework chat for a specific homework.
     *
     * @param homeworkId the ID of the homework
     * @return the UUID of the uncompleted homework chat, or null if none exists
     * Pre-condition: The `homeworkId` must not be null. The homework associated with the `homeworkId` must exist.
     * Post-condition: Returns the UUID of the uncompleted homework chat if one exists, otherwise null.
     */
    UUID hasUncompletedHomeworkChat(@NotNull Integer homeworkId);

    /**
     * Retrieves the chat ID for a specific student's homework chat.
     *
     * @param homeworkId the ID of the homework
     * @param studentId  the UUID of the student
     * @return the UUID of the student's homework chat
     * Pre-condition: The `homeworkId` and `studentId` must not be null. The caller must have permission to access the specified student's homework chat.
     * Post-condition: Returns the UUID of the homework chat associated with the specified student and homework, or throws an exception if not found.
     */
    UUID getStudentHomeworkChatId(@NotNull Integer homeworkId, @NotNull UUID studentId);

    /**
     * Checks if the specific homework chat has been marked as completed.
     *
     * @param chatId the UUID of the chat
     * @return `true` if the chat is completed, `false` otherwise
     * Pre-condition: The `chatId` must not be null. The chat associated with the `chatId` must exist.
     * Post-condition: Returns the completion status of the chat.
     */
    boolean isCompleted(@NotNull UUID chatId);

    /**
     * Marks a specific homework chat as completed.
     *
     * @param chatId the UUID of the chat
     * @return `true` if the chat was successfully marked as completed, `false` otherwise
     * Pre-condition: The `chatId` must not be null. The chat associated with the `chatId` must exist.
     * Post-condition: Marks the chat as completed and saves the update.
     */
    boolean complete(@NotNull UUID chatId);
}
