package it.astromark.authentication.controller;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.authentication.service.UserLoginDTO;
import it.astromark.school.SchoolRepository;
import it.astromark.school.entity.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {

        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginDTO user) {
        return authenticationService.login(user.getPassword(), user.getPassword(), user.getSchoolCode() , user.getRole()).toString();
    }

    @GetMapping("/login")
    public String Hello() {
        return "Hello World";
    }

}
