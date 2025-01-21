package it.astromark.chat.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.chat.dto.MessageResponse;
import it.astromark.chat.entity.Message;
import it.astromark.chat.mapper.ChatMapper;
import it.astromark.chat.repository.HomeworkChatRepository;
import it.astromark.chat.repository.MessageRepository;
import it.astromark.chat.repository.TicketRepository;
import it.astromark.commons.service.FileService;
import it.astromark.user.commons.model.SchoolUser;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatMapper chatMapper;
    private final AuthenticationService authenticationService;
    private final FileService fileService;
    private final HomeworkChatRepository homeworkChatRepository;
    private final TicketRepository ticketRepository;

    public MessageServiceImpl(MessageRepository messageRepository, ChatMapper chatMapper, AuthenticationService authenticationService, FileService fileService, HomeworkChatRepository homeworkChatRepository, TicketRepository ticketRepository) {
        this.messageRepository = messageRepository;
        this.chatMapper = chatMapper;
        this.authenticationService = authenticationService;
        this.fileService = fileService;
        this.homeworkChatRepository = homeworkChatRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public SchoolUser getSender(Message message) {
        if (message.getStudent() != null) {
            return message.getStudent();
        } else if (message.getParent() != null) {
            return message.getParent();
        } else if (message.getTeacher() != null) {
            return message.getTeacher();
        } else if (message.getSecretary() != null) {
            return message.getSecretary();
        }
        return null;
    }

    @Override
    public MessageResponse create(String message, UUID chatId, boolean isHomework) {
        if(isHomework) {
            var save = Message.builder()
                    .text(message)
                    .homeworkChat(homeworkChatRepository.findById(chatId).orElseThrow())
                    .student(authenticationService.getStudent().orElse(null))
                    .parent(authenticationService.getParent().orElse(null))
                    .teacher(authenticationService.getTeacher().orElse(null))
                    .secretary(authenticationService.getSecretary().orElse(null))
                    .dateTime(Instant.now())
                    .build();
            return chatMapper.toMessageResponse(messageRepository.save(save), this);
        }
        else {
            var save = Message.builder()
                    .text(message)
                    .ticket(ticketRepository.findById(chatId).orElseThrow())
                    .student(authenticationService.getStudent().orElse(null))
                    .parent(authenticationService.getParent().orElse(null))
                    .teacher(authenticationService.getTeacher().orElse(null))
                    .secretary(authenticationService.getSecretary().orElse(null))
                    .dateTime(Instant.now())
                    .build();
            return chatMapper.toMessageResponse(messageRepository.save(save), this);
        }
    }

    @Override
    @Transactional
    public String addAttachment(UUID uuid, MultipartFile multipartFile) throws IOException {
        var message = messageRepository.findById(uuid).orElseThrow();
        if(authenticationService.isStudent() && !authenticationService.getStudent().orElseThrow().equals(message.getStudent())){
            throw new IllegalArgumentException("You can't add an attachment to a message that is not yours");
        } else if (authenticationService.isParent() && !authenticationService.getParent().orElseThrow().equals(message.getParent())){
            throw new IllegalArgumentException("You can't add an attachment to a message that is not yours");
        } else if (authenticationService.isTeacher() && !authenticationService.getTeacher().orElseThrow().equals(message.getTeacher())){
            throw new IllegalArgumentException("You can't add an attachment to a message that is not yours");
        } else if (authenticationService.isSecretary() && !authenticationService.getSecretary().orElseThrow().equals(message.getSecretary())){
            throw new IllegalArgumentException("You can't add an attachment to a message that is not yours");
        }
        message.setAttachment(fileService.uploadFile(multipartFile));
        messageRepository.save(message);
        return message.getAttachment();
    }


}
