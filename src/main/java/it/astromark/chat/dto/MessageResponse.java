package it.astromark.chat.dto;

import java.time.Instant;
import java.util.UUID;

public record MessageResponse(UUID id, String senderName, Boolean isSecretary, Instant dateTime, String text, String attachment) {

}
