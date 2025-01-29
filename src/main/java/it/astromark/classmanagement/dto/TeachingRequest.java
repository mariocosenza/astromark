package it.astromark.classmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "Request containing details for adding a teaching activity")
public record TeachingRequest(
       @NotEmpty String subjectTitle,
       @NotEmpty String activityType
) {
}
