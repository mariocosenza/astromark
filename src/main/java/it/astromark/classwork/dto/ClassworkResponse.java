package it.astromark.classwork.dto;

import it.astromark.agenda.schoolclass.dto.TeachingTimeslotResponse;

import java.time.LocalDate;

public record ClassworkResponse(Integer id, String title, String description, TeachingTimeslotResponse signedHour) {
}
