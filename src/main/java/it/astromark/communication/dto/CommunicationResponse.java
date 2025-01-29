package it.astromark.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Response object containing details of a communication")
public record CommunicationResponse(
        Integer id,
        String title,
        LocalDate date,
        String description
) {
}
