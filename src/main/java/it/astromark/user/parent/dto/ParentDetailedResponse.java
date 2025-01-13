package it.astromark.user.parent.dto;

import java.time.LocalDate;

public record ParentDetailedResponse(String name, String surname, String email, String residentialAddress, String taxId, LocalDate birthDate, Boolean gender, Boolean legalGuardian) {
}
