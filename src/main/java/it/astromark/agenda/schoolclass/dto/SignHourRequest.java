package it.astromark.agenda.schoolclass.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for signing a teaching hour")
public record SignHourRequest(
        Integer slotId,
        Integer hour,
        Date date,
        String activityTitle,
        String activityDescription,
        String homeworkTitle,
        String homeworkDescription,
        Date homeworkDueDate
) {
}
