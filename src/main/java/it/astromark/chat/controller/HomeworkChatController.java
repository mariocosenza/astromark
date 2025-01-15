package it.astromark.chat.controller;

import it.astromark.chat.dto.MessageResponse;
import it.astromark.chat.service.HomeworkChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/homeworks")
public class HomeworkChatController {


    private final HomeworkChatService homeworkChatService;

    public HomeworkChatController(HomeworkChatService homeworkChatService) {
        this.homeworkChatService = homeworkChatService;
    }

    @PostMapping("{homeworkId}/chats")
    public void createHomeworkChat(@PathVariable Integer homeworkId) {
        homeworkChatService.addChat(homeworkId);
    }

    @PostMapping("/{chatId}/addMessage")
    public UUID sendMessage(@PathVariable UUID chatId, @RequestBody  String messageRequest) {
        return homeworkChatService.sendMessage(chatId, messageRequest.replace("\"", ""));
    }

    @GetMapping("/{chatId}/messages")
    public List<MessageResponse> getMessageList(@PathVariable UUID chatId) {
       return homeworkChatService.getMessageList(chatId);
    }

    @GetMapping("/{homeworkId}/has-uncompleted-chat")
    public boolean hasUncompletedHomeworkChat (@PathVariable Integer homeworkId) {
        return homeworkChatService.hasUncompletedHomeworkChat(homeworkId);
    }

}
