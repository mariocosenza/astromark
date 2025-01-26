package it.astromark.user.parent.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Detailed response containing information about a parent")
public record ParentDetailedResponse(
        String name,
        String surname,
        String email,
        String residentialAddress,
        String taxId,
        LocalDate birthDate,
        Boolean male,
        Boolean legalGuardian
) {
}
