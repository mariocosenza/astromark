package it.astromark.rating.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.astromark.rating.model.MarkType;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Response object containing details of a student's rating")
public record RatingsResponse(
        Integer id,
        UUID studentId,
        String name,
        String surname,
        String subject,
        Double mark,
        MarkType type,
        String description,
        LocalDate date
) {
}
