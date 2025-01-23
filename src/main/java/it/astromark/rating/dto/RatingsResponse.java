package it.astromark.rating.dto;

import it.astromark.rating.model.MarkType;

import java.time.LocalDateTime;
import java.util.UUID;

public record RatingsResponse(Integer id, UUID studentId, String name, String surname, Double mark, MarkType type, String description, LocalDateTime date) {
}
