package it.astromark.agenda.schoolclass.dto;

import java.time.LocalDate;

public record ClassTimeTableRequest(String schoolClass , LocalDate startDate, LocalDate endDate) { }
