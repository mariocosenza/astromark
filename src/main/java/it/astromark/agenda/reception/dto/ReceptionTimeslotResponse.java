package it.astromark.agenda.reception.dto;

import java.time.LocalDate;

public record ReceptionTimeslotResponse(Integer id, Short capacity, Short booked, String mode, Short hour, LocalDate date, String name, String surname) {
}
