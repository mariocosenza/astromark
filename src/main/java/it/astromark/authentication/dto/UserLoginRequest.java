package it.astromark.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request object for user login containing credentials and role information.")
public record UserLoginRequest(
        @NotEmpty
        @Size(max = 256)
        @Size(min = 5)
        String username,

        @Size(max = 512)
        @Size(min = 8)
        String password,

        @Size(max = 7)
        @Pattern(regexp = "^SS\\d{5}$")
        String schoolCode,

        @NotEmpty
        String role
) {
}
