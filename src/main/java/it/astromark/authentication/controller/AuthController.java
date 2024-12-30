package it.astromark.authentication.controller;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.authentication.service.UserLoginDTO;
import it.astromark.school.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthenticationService authenticationService;
    private final SchoolRepository schoolRepository;

    @Autowired
    public AuthController(final AuthenticationService authenticationService , final SchoolRepository schoolRepository) {

        this.authenticationService = authenticationService;
        this.schoolRepository = schoolRepository;
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginDTO user) {
        return authenticationService.login(user.getPassword(), user.getPassword(), user.getSchoolCode() , user.getRole()).toString();
    }

    @GetMapping("/login")
    public String Hello() {
        return schoolRepository.findByCode("SS12345").toString();
    }

}
