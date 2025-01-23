package it.astromark.agenda.reception.dto;

import it.astromark.agenda.reception.entity.ReceptionBookingId;

import java.time.LocalDate;

public record ReceptionBookingResponse(ReceptionBookingId id, Short bookingOrder, Boolean confirmed, Boolean refused,
                                       LocalDate date, Short hour, String mode, String name, String surname) {
}
