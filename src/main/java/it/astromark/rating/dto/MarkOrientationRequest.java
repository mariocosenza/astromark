package it.astromark.rating.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request containing details for marking an orientation")
public record MarkOrientationRequest(
        String title,
        Double mark
) {
}
