package it.astromark.agenda.schoolclass.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Request object for creating or updating a teaching timeslot")
public record TeachingTimeslotRequest(Integer dayWeek , Short hour , UUID idTeacher , String subject) {
}

