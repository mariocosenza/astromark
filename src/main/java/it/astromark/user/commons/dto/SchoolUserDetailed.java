package it.astromark.user.commons.dto;

import java.time.LocalDate;
import java.util.UUID;

public record SchoolUserDetailed(UUID id, String name, String surname, String email, String residentialAddress, String taxId, LocalDate birthDate, Boolean male) {
}
