package it.astromark.agenda.schoolclass.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Response object containing detailed information about a teaching timeslot")
public record TeachingTimeslotDetailedResponse(
        Integer id,
        Integer hour,
        UUID teacherId,
        String name,
        String surname,
        String subject,
        Boolean signed,
        String activityTitle,
        String activityDescription,
        String homeworkTitle,
        String homeworkDescription,
        LocalDate homeworkDueDate,
        Boolean homeworkNeedChat
) {
}
