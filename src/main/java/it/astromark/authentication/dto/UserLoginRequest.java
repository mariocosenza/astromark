package it.astromark.authentication.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserLoginRequest(@NotEmpty @Size(max = 256) @Size(min = 5) String username, @Size(max = 512) @Size(min = 8)
@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
        message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, one special character, and no whitespace.") String password,
                               @Size(max = 7) @Pattern(regexp = "^SS\\d{5}$") String schoolCode,
                               @NotEmpty String role) {
}



