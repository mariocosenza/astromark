package it.astromark.user.commons.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request containing details for updating a school user's preferences")
public record SchoolUserUpdate(
        String password
) {
}
