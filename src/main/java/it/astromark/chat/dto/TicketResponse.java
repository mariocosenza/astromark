package it.astromark.chat.dto;

import java.time.Instant;
import java.util.UUID;

public record TicketResponse(UUID id, String title, Instant datetime) {

}
