package it.astromark.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Response object containing details of a chat message")
public record MessageResponse(
        UUID id,
        String senderName,
        Boolean isSecretary,
        Instant dateTime,
        String text,
        String attachment
) {
}
