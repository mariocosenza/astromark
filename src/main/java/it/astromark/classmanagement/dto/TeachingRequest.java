package it.astromark.classmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request containing details for adding a teaching activity")
public record TeachingRequest(
        String subjectTitle,
        String activityType
) {
}
