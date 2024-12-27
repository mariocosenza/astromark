package it.astromark.commons.exception;

import jakarta.validation.constraints.NotNull;


public record ExceptionResponse(int status, @NotNull String message) {
}
