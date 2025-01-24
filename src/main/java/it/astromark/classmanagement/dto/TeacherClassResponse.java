package it.astromark.classmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object containing details of a class assigned to a teacher")
public record TeacherClassResponse(
        Integer id,
        Short number,
        String letter,
        String description
) {
}
