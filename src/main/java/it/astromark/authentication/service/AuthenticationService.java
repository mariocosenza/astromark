package it.astromark.authentication.service;

import it.astromark.school.entity.School;
import it.astromark.user.commons.model.SchoolUser;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService  {

    SchoolUser login(String username, String password , School schoolCode);

}
