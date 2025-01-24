package it.astromark.rating.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.astromark.rating.model.MarkType;

import java.time.LocalDate;

@Schema(description = "Response object containing details of a mark")
public record MarkResponse(
        String title,
        Double mark,
        String description,
        MarkType type,
        LocalDate date
) {
}
