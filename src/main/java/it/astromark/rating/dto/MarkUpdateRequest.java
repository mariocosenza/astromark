package it.astromark.rating.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.astromark.rating.model.MarkType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Request object for updating an existing mark")
public record MarkUpdateRequest(
        Integer id,
        String description,
        @PositiveOrZero Double mark,
        @NotNull MarkType type
) {
}
