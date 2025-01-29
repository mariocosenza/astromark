package it.astromark.behavior.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Request object for creating or updating a note")
public record NoteRequest(
        @NotNull UUID studentId,
        @Size(max = 512)
        @NotNull String description,
        @PastOrPresent @NotNull LocalDate date
) {
}
