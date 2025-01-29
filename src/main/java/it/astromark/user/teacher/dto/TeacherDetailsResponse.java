package it.astromark.user.teacher.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response containing details of a teacher and their teaching subjects")
public record TeacherDetailsResponse(
        String username,
        List<String> teaching
) {
}
