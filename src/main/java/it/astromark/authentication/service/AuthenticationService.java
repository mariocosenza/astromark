package it.astromark.authentication.service;

import it.astromark.user.commons.model.SchoolUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface AuthenticationService  {

    SchoolUser login(String username, String password , String schoolCode , String role);
    SchoolUser getUser(UUID id , String role);
    String verify(String username, String password, String schoolCode, String role);

    GrantedAuthority getRole(SchoolUser user);




}
