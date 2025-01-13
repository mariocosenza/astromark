package it.astromark.commons.dto;

public record APIResponse(String message, boolean isSuccessful, int statusCode, Object data) {
}
