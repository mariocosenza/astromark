package it.astromark.chat.controller;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.authentication.service.JWTService;
import it.astromark.chat.dto.HomeworkChatResponse;
import it.astromark.chat.service.HomeworkChatService;
import it.astromark.chat.service.MessageService;
import it.astromark.user.student.entity.Student;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@Controller
public class WebSocketChatController {

    private final HomeworkChatService homeworkChatService;
    private final JWTService jwtService;
    private final AuthenticationService authenticationService;

    public WebSocketChatController(SimpMessagingTemplate messagingTemplate, HomeworkChatService homeworkChatService, MessageService messageService, JWTService jwtService, AuthenticationService authenticationService) {
        this.homeworkChatService = homeworkChatService;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @MessageMapping("homeworks/chats/{chatId}")
    @SendTo("/topic/chat/{chatId}") // Added leading '/'
    @Transactional
    public HomeworkChatResponse handleMessage(@DestinationVariable UUID chatId,
                                              @Header("Authorization") String token) {
        token = token.substring(7); // Remove "Bearer " prefix
        var user = authenticationService.getUser(jwtService.extractUUID(token), jwtService.extractRole(token));

        if (jwtService.validateToken(token, user)) {
            if (!(user instanceof Student) && !(user instanceof Teacher)) {
                throw new IllegalArgumentException("You are not a student or a teacher");
            } else if (user instanceof Student) {
                if (((Student) user).getHomeworkChats().stream().noneMatch(homeworkChat -> homeworkChat.getId().equals(chatId))) {
                    throw new IllegalArgumentException("You are not part of this chat");
                }
            } else { // Teacher
                if (!homeworkChatService.findTeacher(chatId).getId().equals(user.getId())) {
                    throw new IllegalArgumentException("You are not part of this chat");
                }
            }

            return homeworkChatService.getChatWithMessagesSocket(chatId);
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }

}
