package it.astromark.chat.controller;
import it.astromark.chat.dto.HomeworkChatDTO;
import it.astromark.chat.dto.MessageDTO;
import it.astromark.chat.service.HomeworkChatService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class WebSocketChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final HomeworkChatService chatService;

    public WebSocketChatController(SimpMessagingTemplate messagingTemplate, HomeworkChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/sendMessage/{chatId}")
    @SendTo("/topic/chat/{chatId}")
    public void handleMessage(@DestinationVariable UUID chatId, MessageDTO messageDTO) {
        chatService.saveMessage(messageDTO);
        HomeworkChatDTO chatDTO = chatService.getChatWithMessages(chatId);
        messagingTemplate.convertAndSend("/topic/chat/" + chatId, chatDTO);
    }
}
