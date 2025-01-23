package it.astromark.rating.dto;

import it.astromark.rating.model.MarkType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record MarkUpdateRequest(Integer id, String description, @PositiveOrZero Double mark, @NotNull MarkType type) {
}
