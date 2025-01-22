package it.astromark.attendance.dto;


import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record JustifiableResponse(@NotNull UUID id, @NotNull Boolean needsJustification, @NotNull Boolean justified,
                                  String justificationText, String date) {
}
