package it.astromark.agenda.reception.dto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Request object for creating or updating a reception timeslot")
public record ReceptionTimeslotRequest(
        Short capacity,
        String mode,
        Short hour,
        LocalDate date
) {
}

