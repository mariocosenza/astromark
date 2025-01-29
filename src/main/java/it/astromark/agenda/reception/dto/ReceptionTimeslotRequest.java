package it.astromark.agenda.reception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Request object for creating or updating a reception timeslot")
public record ReceptionTimeslotRequest(
       @Positive @Max(50) Short capacity,
        @NotEmpty String mode,
        @Min(1) @Max(8) Short hour,
        @FutureOrPresent LocalDate date
) {
}

