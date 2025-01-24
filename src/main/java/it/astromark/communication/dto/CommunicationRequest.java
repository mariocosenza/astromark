package it.astromark.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for creating or updating a communication")
public record CommunicationRequest() {
}
