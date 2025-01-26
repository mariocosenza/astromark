package it.astromark.classmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request containing details for adding a teacher to a class")
public record AddToClassRequest(
        Integer classId,
        boolean isCoordinator
) {
}
