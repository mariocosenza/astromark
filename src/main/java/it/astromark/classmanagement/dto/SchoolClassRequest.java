package it.astromark.classmanagement.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request containing details for creating a school class")
public record SchoolClassRequest(
        @PositiveOrZero
        Short number,

        @Size(max = 2)
        @NotNull
        @Pattern(regexp = "^[A-Z]{1,2}$", message = "One to two alphabet letters allowed")
        String letter
) {
}
