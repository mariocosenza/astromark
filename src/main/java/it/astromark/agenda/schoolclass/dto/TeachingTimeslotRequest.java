package it.astromark.agenda.schoolclass.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "Request object for creating or updating a teaching timeslot")
public record TeachingTimeslotRequest(@Min(0) @Max(6) Integer dayWeek, @Min(1) @Max(8) Short hour, String username, String subject,
                                      Integer timetableId) {
}

