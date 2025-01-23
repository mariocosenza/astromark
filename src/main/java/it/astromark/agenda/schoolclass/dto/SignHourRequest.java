package it.astromark.agenda.schoolclass.dto;

import java.util.Date;

public record SignHourRequest(Integer slotId, Integer hour, Date date, String activityTitle, String activityDescription, String homeworkTitle, String homeworkDescription, Date homeworkDueDate) {
}
