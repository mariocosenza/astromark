package it.astromark.classmanagement.dto;

import jakarta.validation.constraints.*;

public record SchoolClassRequest(@PositiveOrZero Short number,
                                 @Size(max = 2) @NotNull @Pattern(regexp = "^[A-Z]{1,2}$", message = "One to two alphabet letter allowed") String letter) { }
