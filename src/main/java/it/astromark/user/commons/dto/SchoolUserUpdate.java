package it.astromark.user.commons.dto;

import jakarta.validation.constraints.Size;

public record SchoolUserUpdate(@Size(max = 512) String password) {
}
