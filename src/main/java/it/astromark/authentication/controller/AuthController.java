package it.astromark.authentication.controller;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.authentication.service.UserLoginDTO;
import it.astromark.school.repository.SchoolRepository;
import it.astromark.user.commons.model.SchoolUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthenticationService authenticationService;
    private final SchoolRepository schoolRepository;

    @Autowired
    public AuthController(final AuthenticationService authenticationService, final SchoolRepository schoolRepository) {

        this.authenticationService = authenticationService;
        this.schoolRepository = schoolRepository;
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginDTO user) {
        SchoolUser schoolUser=  authenticationService.login(user.getUsername(), user.getPassword(), user.getSchoolCode(), user.getRole());
        if(schoolUser != null) {
            return "Login successful";
        }
        else
            return "Login failed";
    }

    @GetMapping("/login")
    public String Hello() {
        return schoolRepository.findByCode("SS12345").toString();
    }

}
