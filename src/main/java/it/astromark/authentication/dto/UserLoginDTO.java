package it.astromark.authentication.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserLoginDTO (@Size(max = 256) String username, @Size(max = 512) String password,
                            @Size(max = 7) @Pattern(regexp = "^SS\\d{5}$") String schoolCode, String role){}



