package it.astromark.authentication.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginDTO {

    private String username;
    private String password;
    private String schoolCode;
    private String role;


}
