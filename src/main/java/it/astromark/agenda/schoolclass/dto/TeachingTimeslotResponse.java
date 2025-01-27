package it.astromark.agenda.schoolclass.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Response object containing basic information about a teaching timeslot")
public record TeachingTimeslotResponse(
        Short hour,
        LocalDate date,
        String title
) {
}
