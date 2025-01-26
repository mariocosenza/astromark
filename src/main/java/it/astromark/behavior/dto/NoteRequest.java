package it.astromark.behavior.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Request object for creating or updating a note")
public record NoteRequest(
        UUID studentId,
        String description,
        LocalDate date
) {
}
