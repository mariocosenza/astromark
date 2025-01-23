package it.astromark.authentication.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserFirstLoginRequest(@NotEmpty @Size(max = 256) String username, @Size(max = 512) @Size(min = 8)
@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
        message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, one special character, and no whitespace.") String password,
                                    @Size(max = 7) @Pattern(regexp = "^SS\\d{5}$") String schoolCode,
                                    @NotEmpty String role,
                                    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                                            message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, one special character, and no whitespace.") String newPassword) {
}
