package it.astromark.agenda.schoolclass.dto;

import java.time.LocalDate;


public record TeachingTimeslotResponse(Short hour, LocalDate date, String title) {
}
