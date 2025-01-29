package it.astromark.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Response object containing details of a ticket")
public record TicketResponse(
        UUID id,
        String title,
        Instant datetime,
        String category,
        Boolean closed,
        Boolean solved,
        Boolean isTeacher
) {
}
