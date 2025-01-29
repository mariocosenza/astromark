package it.astromark.classmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "Request containing details for adding a teacher to a class")
public record AddToClassRequest(
        @NotEmpty Integer classId,
        boolean isCoordinator
) {
}
