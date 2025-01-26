package it.astromark.classwork.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.astromark.agenda.schoolclass.dto.TeachingTimeslotResponse;

@Schema(description = "Response object containing details of a classwork activity")
public record ClassworkResponse(
        Integer id,
        String title,
        String description,
        TeachingTimeslotResponse signedHour
) {
}
