package it.astromark.agenda.schoolclass.dto;

import java.util.Date;

public record SignHourRequest(Integer slotId, String activityTitle, String activityDescription, String homeworkTitle, String homeworkDescription, Date homeworkDueDate) {
}
