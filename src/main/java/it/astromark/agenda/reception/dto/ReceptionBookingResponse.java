package it.astromark.agenda.reception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.astromark.agenda.reception.entity.ReceptionBookingId;

import java.time.LocalDate;

@Schema(description = "Response containing details of a reception booking")
public record ReceptionBookingResponse(
        ReceptionBookingId id,
        Short bookingOrder,
        Boolean confirmed,
        Boolean refused,
        LocalDate date,
        Short hour,
        String mode,
        String name,
        String surname
) {
}

