package it.astromark.classwork.dto;

import it.astromark.agenda.schoolclass.dto.TeachingTimeslotResponse;

public record ClassworkResponse(Integer id, String title, String description, TeachingTimeslotResponse signedHour) {
}
