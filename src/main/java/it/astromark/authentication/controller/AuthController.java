package it.astromark.authentication.controller;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.school.SchoolRepository;
import it.astromark.school.entity.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private SchoolRepository schoolRepository;



    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("schoolCode") String code) {
        School school = schoolRepository.findByCode(code);
        if (school == null) {
            return "School not found";
        }
        return authenticationService.login(username, password, school).toString();
    }

    @GetMapping("/login")
    public String Hello() {
        return "Hello World";
    }

}
