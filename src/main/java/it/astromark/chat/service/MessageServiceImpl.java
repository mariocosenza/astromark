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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

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
    public SchoolUser getSender(@NotNull Message message) {
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
    public MessageResponse create(@NotEmpty String message, @NotNull UUID chatId, boolean isHomework) {
        if (isHomework) {
            var homeworkChat = homeworkChatRepository.findById(chatId).orElseThrow();
            if (homeworkChat.getCompleted()) {
                throw new IllegalArgumentException("Chat is completed");
            }
            var save = Message.builder()
                    .text(message)
                    .homeworkChat(homeworkChat)
                    .student(authenticationService.getStudent().orElse(null))
                    .parent(authenticationService.getParent().orElse(null))
                    .teacher(authenticationService.getTeacher().orElse(null))
                    .secretary(authenticationService.getSecretary().orElse(null))
                    .dateTime(Instant.now())
                    .build();
            return chatMapper.toMessageResponse(messageRepository.save(save), this);
        } else {
            var ticket = ticketRepository.findById(chatId).orElseThrow();
            if (ticket.getClosed()) {
                throw new IllegalArgumentException("Ticket is closed");
            }
            var save = Message.builder()
                    .text(message)
                    .ticket(ticket)
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
    @PreAuthorize("hasRole('STUDENT') || hasRole('TEACHER')")
    public String addAttachment(@NotNull UUID uuid, @NotNull MultipartFile multipartFile) throws IOException {
        var message = messageRepository.findById(uuid).orElseThrow();
        Supplier<IllegalArgumentException> exceptionSupplier = () -> new IllegalArgumentException("You can't add an attachment to a message that is not yours");
        if (authenticationService.isStudent() && !authenticationService.getStudent().orElseThrow().getId().equals(message.getStudent().getId())) {
            throw exceptionSupplier.get();
        } else if (authenticationService.isTeacher() && !authenticationService.getTeacher().orElseThrow().getId().equals(message.getTeacher().getId())) {
            throw exceptionSupplier.get();
        }
        message.setAttachment(fileService.uploadFile(multipartFile));
        messageRepository.save(message);
        return message.getAttachment();
    }


}
