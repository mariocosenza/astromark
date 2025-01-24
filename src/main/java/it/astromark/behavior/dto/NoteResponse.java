package it.astromark.behavior.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Response object containing details of a note")
public record NoteResponse(
        UUID id,
        LocalDate date,
        String description,
        boolean viewed
) {
}
