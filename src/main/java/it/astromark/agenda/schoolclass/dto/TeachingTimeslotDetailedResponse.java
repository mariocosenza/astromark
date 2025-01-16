package it.astromark.agenda.schoolclass.dto;

import java.time.LocalDate;
import java.util.UUID;

public record TeachingTimeslotDetailedResponse(Integer id, Integer hour, UUID teacherId, String name, String surname, String subject, Boolean signed, String activityTitle, String activityDescription, String homeworkTitle, String homeworkDescription, LocalDate homeworkDueDate) {

}
