package it.astromark.classmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Response object containing details of a student in the class")
public record SchoolClassStudentResponse(
        UUID id,
        String name,
        String surname
) {
}