package it.astromark.agenda.schoolclass.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request containing details for creating a class timetable")
public record ClassTimeTableRequest(
        Integer schoolClassId,
        LocalDate startDate,
        LocalDate endDate,
        Short expectedHours
) {
}
