package it.astromark.agenda.schoolclass.dto;

import java.time.LocalDate;

public record TimeTableResponse(Integer schoolClassID , LocalDate startDate, LocalDate endDate , @jakarta.validation.constraints.NotNull @jakarta.validation.constraints.PositiveOrZero Short number , String letter , Integer year) {
}
