package it.astromark.user.commons.dto;

import java.time.LocalDate;

public record SchoolUserDetailed(String name, String surname, String email, String residentialAddress, String taxId, LocalDate birthDate, Boolean gender) {
}
