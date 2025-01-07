package it.astromark.user.commons.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.user.parent.entity.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SchoolUserServiceImpl implements SchoolUserService {

    private final AuthenticationService authenticationService;

    @Autowired
    public SchoolUserServiceImpl(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean isStudentParent(Parent parent, UUID studentId) {
        return parent.getStudents().stream().noneMatch(s -> s.getId().equals(studentId));
    }

    @Override
    public boolean isLoggedUserParent(UUID studentId) {
        if(authenticationService.isParent() && isStudentParent(authenticationService.getParent().orElseThrow(), studentId)) {
            return false;
        }
        return true;
    }
}
