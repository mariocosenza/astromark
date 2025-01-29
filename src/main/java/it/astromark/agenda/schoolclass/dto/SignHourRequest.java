package it.astromark.agenda.schoolclass.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.astromark.classwork.dto.ClassActivityRequest;
import it.astromark.classwork.dto.HomeworkRequest;

import java.time.LocalDate;

@Schema(description = "Request object for signing a teaching hour")
public record SignHourRequest(
        Integer id,
        Integer hour,
        String subject,
        LocalDate date,
        ClassActivityRequest activity,
        HomeworkRequest homework
) {
}
