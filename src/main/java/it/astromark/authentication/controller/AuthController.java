package it.astromark.authentication.controller;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.authentication.dto.UserLoginDTO;
import it.astromark.authentication.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final JWTService jwtService;

    @Autowired
    public AuthController(final AuthenticationService authenticationService, JWTService jwtService) {

        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO user) {
        var schoolUser = authenticationService.login(user.username(), user.password(), user.schoolCode(), user.role());
        if (schoolUser != null) {
            return switch (schoolUser.getPendingState()) {
                case FIRST_LOGIN -> new ResponseEntity<>("Must change password", HttpStatus.NOT_ACCEPTABLE);
                case NORMAL, REMOVE ->
                        new ResponseEntity<>(authenticationService.verify(user.username(), user.password(), user.schoolCode(), user.role()), HttpStatus.OK);
            };
        }

        return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/first-login")
    public String Hello() {
        return "First login logic";
    }

    @GetMapping("/token")
    public String sayHello() {
        return "Hello";

    }

    @PostMapping("/logout")
    public String logout(@RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7); // Rimuove "Bearer "
            jwtService.logOut(jwtToken);
            return "Logout successful";
        } else {
            return "Logout Error: Authorization header missing or invalid";

        }
    }
}



