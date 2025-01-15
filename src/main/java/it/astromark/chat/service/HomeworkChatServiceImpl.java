package it.astromark.chat.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.chat.dto.HomeworkChatResponse;
import it.astromark.chat.dto.MessageResponse;
import it.astromark.chat.entity.HomeworkChat;
import it.astromark.chat.entity.Message;
import it.astromark.chat.mapper.ChatMapper;
import it.astromark.chat.repository.HomeworkChatRepository;
import it.astromark.chat.repository.MessageRepository;
import it.astromark.classwork.repository.HomeworkRepository;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;


@Service
public class HomeworkChatServiceImpl implements HomeworkChatService {

    private final HomeworkChatRepository homeworkChatRepository;
    private final ChatMapper chatMapper;
    private final MessageService messageService;
    private final HomeworkRepository homeworkRepository;
    private final AuthenticationService authenticationService;
    private final MessageRepository messageRepository;

    public HomeworkChatServiceImpl(HomeworkChatRepository homeworkChatRepository, ChatMapper chatMapper, MessageService messageService, HomeworkRepository homeworkRepository, AuthenticationService authenticationService, MessageRepository messageRepository) {
        this.homeworkChatRepository = homeworkChatRepository;
        this.chatMapper = chatMapper;
        this.messageService = messageService;
        this.homeworkRepository = homeworkRepository;
        this.authenticationService = authenticationService;
        this.messageRepository = messageRepository;
    }

    public HomeworkChatResponse getChatWithMessagesSocket(UUID chatId) {
        HomeworkChat chat = homeworkChatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        return new HomeworkChatResponse(
                chat.getId(),
                chat.getTitle(),
                chat.getStudent().getId(),
                chat.getCompleted(),
                chatMapper.toMessageResponseList(chat.getMessages().stream().toList().stream().sorted(Comparator.comparing(Message::getDateTime)).toList(), messageService)
        );
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('student') || hasRole('teacher')")
    public UUID sendMessage(UUID chatId, @NotEmpty String text) {
        if(authenticationService.isStudent() && !homeworkChatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found")).getStudent().equals(authenticationService.getStudent().orElseThrow())) {
            throw new AccessDeniedException("You are not allowed to access this chat");
        } else if(authenticationService.isTeacher() && !homeworkChatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found")).getHomeworkSignedHourTeachingTimeslot().getSignedHour().getTeacher().equals(authenticationService.getTeacher().orElseThrow())) {
            throw new AccessDeniedException("You are not allowed to access this chat");
        }
        var message = messageRepository.findById(messageService.create(text, chatId, true).id())
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        return message.getId();
    }

    @Override
    @Transactional
    public Teacher findTeacher(UUID chatId) {
        HomeworkChat chat = homeworkChatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        var teacher = chat.getHomeworkSignedHourTeachingTimeslot().getSignedHour().getTeacher();
        if(teacher== null) {
            throw new IllegalArgumentException("Teacher not found");
        }

        return teacher;
    }

    @Override
    @PreAuthorize("hasRole('teacher')")
    public void addChat(@NotNull Integer homeworkId) {
        var homework = homeworkRepository.findById(homeworkId).orElseThrow();
        if(!homework.getSignedHour().getTeachingTimeslot().getTeaching().getTeacher().equals(authenticationService.getTeacher().orElseThrow())) {
            throw new AccessDeniedException("You are not allowed to access this homework");
        }
        for(var student : homework.getSignedHour().getTeachingTimeslot().getClassTimetable().getSchoolClass().getStudents()) {
            var chat = HomeworkChat.builder()
                    .homeworkSignedHourTeachingTimeslot(homework)
                    .completed(false)
                    .student(student)
                    .build();
            homeworkChatRepository.save(chat);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('student') || hasRole('teacher')")
    public List<MessageResponse> getMessageList(UUID chatId) {
        if(authenticationService.isStudent() && !homeworkChatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found")).getStudent().equals(authenticationService.getStudent().orElseThrow())) {
            throw new AccessDeniedException("You are not allowed to access this chat");
        } else if(authenticationService.isTeacher() && !homeworkChatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found")).getHomeworkSignedHourTeachingTimeslot().getSignedHour().getTeacher().equals(authenticationService.getTeacher().orElseThrow())) {
            throw new AccessDeniedException("You are not allowed to access this chat");
        }
        return chatMapper.toMessageResponseList(messageRepository.findByHomeworkChat_IdOrderByDateTimeAsc(chatId), messageService);
    }

    @Override
    @PreAuthorize("hasRole('student') || hasRole('teacher')")
    public boolean hasUncompletedHomeworkChat(@NotNull Integer homeworkId) {
        var homework = homeworkRepository.findById(homeworkId).orElse(null);
        if(homework == null) {
            return false;
        } else if (authenticationService.isStudent() && !homework.getSignedHour().getTeachingTimeslot().getClassTimetable().getSchoolClass().getStudents().contains(authenticationService.getStudent().orElseThrow())) {
            return false;
        } else if (authenticationService.isTeacher() && !homework.getSignedHour().getTeachingTimeslot().getTeaching().getTeacher().equals(authenticationService.getTeacher().orElseThrow())) {
            return false;
        }

        return homeworkChatRepository.existsHomeworkChatByHomeworkSignedHourTeachingTimeslot_IdAndCompletedIsFalse(homeworkId);
    }
}
