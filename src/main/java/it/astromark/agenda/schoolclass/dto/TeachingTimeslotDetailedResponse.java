package it.astromark.agenda.schoolclass.dto;

public record TeachingTimeslotDetailedResponse(Integer id, Integer hour, String name, String surname, String subject, Boolean signed, String activityTitle, String activityDescription, String homeworkTitle, String homeworkDescription) {

}
