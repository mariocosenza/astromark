package it.astromark.classmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object containing details of a school class")
public record SchoolClassResponse(
        Integer id,
        Integer year,
        String letter,
        Short number,
        String description
) {
}
