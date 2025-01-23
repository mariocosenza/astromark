package it.astromark.chat.service;

import it.astromark.chat.dto.HomeworkChatResponse;
import it.astromark.chat.dto.MessageResponse;
import it.astromark.user.teacher.entity.Teacher;

import java.util.List;
import java.util.UUID;

public interface HomeworkChatService {
    HomeworkChatResponse getChatWithMessagesSocket(UUID chatId);

    UUID sendMessage(UUID chatId, String text);

    Teacher findTeacher(UUID chatId);

    void addChat(Integer homeworkId);

    List<MessageResponse> getMessageList(UUID chatId);

    UUID hasUncompletedHomeworkChat(Integer homeworkId);
}
