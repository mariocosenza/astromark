package it.astromark.rating.dto;

import it.astromark.rating.model.MarkType;

import java.time.LocalDate;

public record MarkResponse(String title, Double mark, String description, MarkType type, LocalDate date) {

}
