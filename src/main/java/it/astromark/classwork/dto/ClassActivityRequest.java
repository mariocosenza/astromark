package it.astromark.classwork.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ClassActivityRequest(
        @NotNull Integer id,
        @NotEmpty String title,
        String description
) {
}
