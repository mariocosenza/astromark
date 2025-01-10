package it.astromark.chat.dto;

import java.time.Instant;

public record MessageResponse(String senderName, Boolean isSecretary, Instant dateTime, String text) {

}
