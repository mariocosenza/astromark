package it.astromark.chat.controller;

import it.astromark.chat.dto.MessageResponse;
import it.astromark.chat.service.HomeworkChatService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "Create a new homework chat",
            description = "Creates a new chat associated with a specific homework by its ID."
    )
    @PostMapping("{homeworkId}/chats")
    public void createHomeworkChat(@PathVariable Integer homeworkId) {
        homeworkChatService.addChat(homeworkId);
    }

    @Operation(
            summary = "Send a message to a chat",
            description = "Sends a message to a specific chat by its ID."
    )
    @PostMapping("/{chatId}/addMessage")
    public UUID sendMessage(@PathVariable UUID chatId, @RequestBody String messageRequest) {
        return homeworkChatService.sendMessage(chatId, messageRequest.replace("\"", ""));
    }

    @Operation(
            summary = "Retrieve messages from a chat",
            description = "Gets a list of all messages in a specific chat by its ID."
    )
    @GetMapping("/{chatId}/messages")
    public List<MessageResponse> getMessageList(@PathVariable UUID chatId) {
        return homeworkChatService.getMessageList(chatId);
    }

    @Operation(
            summary = "Check for uncompleted homework chat",
            description = "Checks if a homework has an associated uncompleted chat and returns its ID if it exists."
    )
    @GetMapping("/{homeworkId}/has-uncompleted-chat")
    public UUID hasUncompletedHomeworkChat(@PathVariable Integer homeworkId) {
        return homeworkChatService.hasUncompletedHomeworkChat(homeworkId);
    }

}
