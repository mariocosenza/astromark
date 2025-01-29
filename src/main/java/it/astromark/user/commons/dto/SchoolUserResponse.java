package it.astromark.user.commons.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Response containing basic information about a school user")
public record SchoolUserResponse(
        String name,
        String surname,
        UUID id
) {
}
