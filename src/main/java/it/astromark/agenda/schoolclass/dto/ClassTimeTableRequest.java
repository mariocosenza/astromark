package it.astromark.agenda.schoolclass.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

@Schema(description = "Request containing details for creating a class timetable")
public record ClassTimeTableRequest(
        @NotNull Integer schoolClassId,
        @NotNull LocalDate startDate,
        LocalDate endDate,
        @PositiveOrZero @Max(40) Short expectedHours
) {
}
