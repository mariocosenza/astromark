package it.astromark.commons.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object for API operations containing a message, success flag, status code, and optional data")
public record APIResponse(
        String message,
        boolean isSuccessful,
        int statusCode,
        Object data
) {
}
