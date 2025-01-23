package it.astromark.rating.dto;

import it.astromark.rating.model.MarkType;

import java.time.LocalDate;
import java.util.UUID;

public record RatingsResponse(Integer id, UUID studentId, String name, String surname, String subject, Double mark, MarkType type, String description, LocalDate date) {
}
