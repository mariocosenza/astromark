package it.astromark.classwork.dto;

import java.time.LocalDate;

public record HomeworkRequest(
        Integer id,
        String title,
        String description,
        LocalDate dueDate,
        Boolean hasChat
) {
}
