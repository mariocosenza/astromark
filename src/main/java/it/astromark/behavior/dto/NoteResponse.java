package it.astromark.behavior.dto;

import java.time.LocalDate;
import java.util.UUID;

public record NoteResponse(UUID id, LocalDate date, String description, boolean viewed) {
}
