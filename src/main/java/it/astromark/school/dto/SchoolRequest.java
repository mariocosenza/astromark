package it.astromark.school.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for creating or updating a school")
public record SchoolRequest() {
}
