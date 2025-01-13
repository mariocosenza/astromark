package it.astromark.chat.dto;

import java.time.Instant;
import java.util.UUID;

public record MessageDTO(
        UUID id,
        String text,
        Instant dateTime,
        String attachment
) {}
