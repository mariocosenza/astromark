package it.astromark.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request object for creating or updating a communication")
public record CommunicationRequest(
        @NotNull Integer classId,
        @NotEmpty String title,
        String description
) {
}
