package it.astromark.user.teacher.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Response containing basic information about a teacher")
public record TeacherResponse(
        String name,
        String surname,
        UUID uuid
) {
}
