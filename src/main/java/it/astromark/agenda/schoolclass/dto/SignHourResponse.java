package it.astromark.agenda.schoolclass.dto;

public record SignHourResponse(Integer hour, String name, String surname, String subject, String activityTitle, String activityDescription, String homeworkTitle, String homeworkDescription) {
}
