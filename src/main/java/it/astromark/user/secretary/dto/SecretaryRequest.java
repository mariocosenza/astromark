package it.astromark.user.secretary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Request containing details for creating a secretary account")
public record SecretaryRequest(
        @Size(max = 256)
        @Email
        String email,

        @Size(max = 64)
        @NotEmpty
        String name,

        @Size(max = 64)
        @NotEmpty
        String surname,

        String taxId,

        @Past
        LocalDate birthDate,

        @NotNull
        Boolean male,

        @Size(min = 5)
        @Pattern(regexp = "^[a-zA-Z0-9\\s.]+$", message = "Address must be at least 5 characters and can only contain letters, numbers, spaces, and periods.")
        String residentialAddress
) {
}
