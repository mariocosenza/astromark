package it.astromark.rating.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object containing details of a mark in a semester report")
public record SemesterReportMarkResponse(
        Integer id,
        String title,
        Short mark
) {
}
