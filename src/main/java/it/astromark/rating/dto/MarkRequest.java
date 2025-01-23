package it.astromark.rating.dto;

import it.astromark.classmanagement.didactic.entity.TeachingId;
import it.astromark.rating.model.MarkType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.util.UUID;

public record MarkRequest(@NotNull UUID studentId, @NotNull TeachingId teachingId, @PastOrPresent LocalDate date, String description, @PositiveOrZero Double mark, @NotNull MarkType type) {
}
