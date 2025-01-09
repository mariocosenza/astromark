package it.astromark.user.commons.dto;

import jakarta.validation.constraints.Size;

public record SchoolUserUpdate(@Size(max = 512) @Size(min = 8) String password) {
}
