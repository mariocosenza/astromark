package it.astromark.chat.service;

import it.astromark.chat.dto.HomeworkChatDTO;
import it.astromark.chat.dto.MessageDTO;

import java.util.UUID;

public interface HomeworkChatService {
    HomeworkChatDTO getChatWithMessages(UUID chatId);
    void saveMessage(MessageDTO messageDTO);
}
