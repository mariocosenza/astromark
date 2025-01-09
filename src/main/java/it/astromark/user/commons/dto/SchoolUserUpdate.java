package it.astromark.user.commons.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SchoolUserUpdate(@Size(max = 512) @Size(min = 8)
                               @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                                       message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, one special character, and no whitespace.")
                               String password) {
}