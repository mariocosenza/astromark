package it.astromark.chat.dto;

import java.util.List;
import java.util.UUID;

public record HomeworkChatResponse(
        UUID id,
        String title,
        UUID studentId,
        Boolean completed,
        List<MessageResponse> messages
) {
}
