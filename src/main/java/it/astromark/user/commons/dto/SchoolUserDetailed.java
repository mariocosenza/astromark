package it.astromark.user.commons.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Detailed information about a school user")
public record SchoolUserDetailed(
        UUID id,
        String name,
        String surname,
        String email,
        String residentialAddress,
        String taxId,
        LocalDate birthDate,
        Boolean male
) {
}
