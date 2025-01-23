package it.astromark.user.commons.controller;

import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.dto.SchoolUserResponse;
import it.astromark.user.commons.dto.SchoolUserUpdate;
import it.astromark.user.commons.service.SchoolUserService;
import jakarta.validation.constraints.Pattern;
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

    @PatchMapping("/preferences")
    public SchoolUserResponse updatePreferences(@RequestBody SchoolUserUpdate schoolUserUpdate) {
        return schoolUserService.updatePreferences(schoolUserUpdate);
    }

    @PatchMapping("/address")
    public SchoolUserResponse updateAddress(@RequestBody @Size(min = 5) @Pattern(regexp = "^[a-zA-Z0-9\\s.]+$") String address) {
        return schoolUserService.updateAddress(address);
    }

    @GetMapping("/detailed")
    public SchoolUserDetailed getByIdDetailed() {
        return schoolUserService.getByIdDetailed();
    }
}
