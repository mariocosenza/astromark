package it.astromark.chat.dto;

import java.util.List;
import java.util.UUID;

public record HomeworkChatDTO(
        UUID id,
        String title,
        UUID studentId,
        Boolean completed,
        List<MessageDTO> messages
) {}
