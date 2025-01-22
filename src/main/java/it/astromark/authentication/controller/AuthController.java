package it.astromark.authentication.controller;

import it.astromark.authentication.dto.UserFirstLoginRequest;
import it.astromark.authentication.dto.UserLoginRequest;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.authentication.service.JWTService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
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
    public ResponseEntity<String> login(@NotNull @RequestBody UserLoginRequest user) {
        var schoolUser = authenticationService.login(user);
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
    public ResponseEntity<String> firstLogin(@RequestBody UserFirstLoginRequest user) {
        var schoolUser = authenticationService.firstLogin(user);
        if (schoolUser != null) {
            return switch (schoolUser.getPendingState()) {
                case FIRST_LOGIN -> new ResponseEntity<>("Must change password", HttpStatus.NOT_ACCEPTABLE);
                case NORMAL, REMOVE ->
                        new ResponseEntity<>(authenticationService.verify(user.username(), user.password(), user.schoolCode(), user.role()), HttpStatus.OK);
            };
        }

        return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7); // Rimuove "Bearer "
            jwtService.logOut(jwtToken);
            return new ResponseEntity<>("Logout successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Logout Error: Authorization header missing or invalid", HttpStatus.BAD_REQUEST);

        }
    }
}



