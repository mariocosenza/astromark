package it.astromark.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

@Schema(description = "Response object containing details of a homework chat, including its messages")
public record HomeworkChatResponse(
        UUID id,
        String title,
        UUID studentId,
        Boolean completed,
        List<MessageResponse> messages
) {
}
