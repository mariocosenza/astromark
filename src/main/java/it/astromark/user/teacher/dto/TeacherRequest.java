package it.astromark.user.teacher.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record TeacherRequest(@Size(max = 256) @Email String email,
                             @Size(max = 64) @NotEmpty String name,
                             @Size(max = 64) @NotEmpty String surname,
                             @NotNull String taxId, @Past LocalDate birthDate,
                             @NotNull boolean male,
                             @Size(min = 5) @Pattern(regexp = "^[a-zA-Z0-9\\s.]+$")
                             String residentialAddress) {
}
