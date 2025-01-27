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

    @Operation(
            summary = "Retrieve student's homework chat ID",
            description = "Gets the chat ID for a specific student's homework chat by the homework ID and student ID."
    )
    @GetMapping("/{homeworkId}/student/{studentId}")
    public UUID getStudentHomeworkChatId(@PathVariable Integer homeworkId, @PathVariable UUID studentId) {
        return homeworkChatService.getStudentHomeworkChatId(homeworkId, studentId);
    }

    @Operation(
            summary = "Check if the homework chat is completed",
            description = "Checks if the specific homework chat, identified by its ID, has been marked as completed."
    )
    @GetMapping("/{chatId}/isCompleted")
    public boolean isCompleted(@PathVariable UUID chatId) {
        return homeworkChatService.isCompleted(chatId);
    }

    @Operation(
            summary = "Mark a homework chat as completed",
            description = "Marks a specific homework chat as completed by its ID. Only the assigned teacher can perform this action."
    )
    @PostMapping("/complete")
    public boolean complete(@RequestBody UUID chatId) {
        return homeworkChatService.complete(chatId);
    }

}
