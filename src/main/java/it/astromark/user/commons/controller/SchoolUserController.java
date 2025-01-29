package it.astromark.user.commons.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.dto.SchoolUserResponse;
import it.astromark.user.commons.dto.SchoolUserUpdate;
import it.astromark.user.commons.service.SchoolUserService;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/school-users")
public class SchoolUserController {

    private final SchoolUserService schoolUserService;

    @Autowired
    public SchoolUserController(SchoolUserService schoolUserService) {
        this.schoolUserService = schoolUserService;
    }

    @Operation(
            summary = "Update user preferences",
            description = "Updates the preferences for the logged-in school user."
    )
    @PatchMapping("/preferences")
    public SchoolUserResponse updatePreferences(@RequestBody SchoolUserUpdate schoolUserUpdate) {
        return schoolUserService.updatePreferences(schoolUserUpdate);
    }

    @Operation(
            summary = "Update user address",
            description = "Updates the address for the logged-in school user."
    )
    @PatchMapping("/address")
    public SchoolUserResponse updateAddress(
            @RequestBody @Size(min = 5) String address) {
        return schoolUserService.updateAddress(address);
    }

    @Operation(
            summary = "Retrieve detailed user information",
            description = "Gets detailed information for the logged-in school user."
    )
    @GetMapping("/detailed")
    public SchoolUserDetailed getByIdDetailed() {
        return schoolUserService.getByIdDetailed();
    }
}
