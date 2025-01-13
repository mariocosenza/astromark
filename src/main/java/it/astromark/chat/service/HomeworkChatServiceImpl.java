package it.astromark.chat.service;

import it.astromark.chat.dto.HomeworkChatDTO;
import it.astromark.chat.dto.MessageDTO;
import it.astromark.chat.entity.HomeworkChat;
import it.astromark.chat.entity.Message;
import it.astromark.chat.repository.HomeworkChatRepository;
import it.astromark.chat.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HomeworkChatServiceImpl implements HomeworkChatService {

    private final HomeworkChatRepository homeworkChatRepository;
    private final MessageRepository messageRepository;

    public HomeworkChatServiceImpl(HomeworkChatRepository homeworkChatRepository, MessageRepository messageRepository) {
        this.homeworkChatRepository = homeworkChatRepository;
        this.messageRepository = messageRepository;
    }

    public HomeworkChatDTO getChatWithMessages(UUID chatId) {
        HomeworkChat chat = homeworkChatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        List<MessageDTO> messages = chat.getMessages().stream()
                .map(message -> new MessageDTO(
                        message.getId(),
                        message.getText(),
                        message.getDateTime(),
                        message.getAttachment()
                ))
                .collect(Collectors.toList());

        return new HomeworkChatDTO(
                chat.getId(),
                chat.getTitle(),
                chat.getStudent().getId(),
                chat.getCompleted(),
                messages
        );
    }

    public void saveMessage(MessageDTO messageDTO) {
        Message message = new Message();
        message.setId(messageDTO.id());
        message.setText(messageDTO.text());
        message.setDateTime(messageDTO.dateTime());
        message.setAttachment(messageDTO.attachment());

        messageRepository.save(message);
    }
}
