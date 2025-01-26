package it.astromark.classmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing details of a teaching activity")
public record TeachingResponse(
        String username,
        String subject
) {
}
