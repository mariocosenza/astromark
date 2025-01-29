package it.astromark.attendance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Response object containing details of a justifiable absence or delay")
public record JustifiableResponse(
        @NotNull UUID id,
        @NotNull Boolean needsJustification,
        @NotNull Boolean justified,
        String justificationText,
        String date
) {
}
