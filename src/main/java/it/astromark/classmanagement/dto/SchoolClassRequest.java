package it.astromark.classmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

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
