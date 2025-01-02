package it.astromark.authentication.controller;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.authentication.service.UserLoginDTO;
import it.astromark.user.commons.model.SchoolUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(final AuthenticationService authenticationService) {

        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginDTO user) {
        SchoolUser schoolUser = authenticationService.login(user.getUsername(), user.getPassword(), user.getSchoolCode(), user.getRole());

        if(schoolUser != null) {
            return switch (schoolUser.getPendingState()){
                case FIRST_LOGIN -> "Must change password";
                case NORMAL, REMOVE -> authenticationService.verify(user.getUsername(), user.getPassword(), user.getSchoolCode(), user.getRole());
            };
        }

        return "Login failed";
    }

    @PostMapping("/first-login")
    public String Hello() {
        return "First login logic";
    }

    @GetMapping("/token")
    public String sayHello(){
        return "Hello";

    }

}
