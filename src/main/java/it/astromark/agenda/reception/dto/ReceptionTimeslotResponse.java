package it.astromark.agenda.reception.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Response containing details of a reception timeslot")
public record ReceptionTimeslotResponse(
        Integer id,
        Short capacity,
        Short booked,
        String mode,
        Short hour,
        LocalDate date,
        String name,
        String surname
) {
}
