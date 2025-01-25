package it.astromark.agenda.schoolclass.dto;

import java.time.LocalDate;

public record ClassTimeTableRequest(Integer schoolClassId , LocalDate startDate, LocalDate endDate , Short expectedHours) { }
