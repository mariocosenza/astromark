package it.astromark.agenda.schoolclass.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Response containing details of a class timetable")
public record TimeTableResponse(
        Integer timeTableId,
        LocalDate startDate,
        LocalDate endDate,
        @jakarta.validation.constraints.NotNull
        @jakarta.validation.constraints.PositiveOrZero
        Short number,
        String letter,
        Integer year
) {
}
