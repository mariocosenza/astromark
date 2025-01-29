package it.astromark.classwork.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record HomeworkRequest(
        @NotNull Integer id,
        @NotEmpty String title,
        String description,
        @FutureOrPresent LocalDate dueDate,
        @NotNull  Boolean hasChat
) {
}
