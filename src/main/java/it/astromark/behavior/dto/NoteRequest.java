package it.astromark.behavior.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for creating or updating a note")
public record NoteRequest() {
}
