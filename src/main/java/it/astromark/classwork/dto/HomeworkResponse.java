package it.astromark.classwork.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.astromark.agenda.schoolclass.dto.TeachingTimeslotResponse;

import java.time.LocalDate;

@Schema(description = "Response object containing details of a homework assignment")
public record HomeworkResponse(
        Integer id,
        String title,
        String description,
        TeachingTimeslotResponse signedHour,
        LocalDate dueDate,
        boolean chat
) {
}
