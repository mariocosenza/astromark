package it.astromark.authentication.service;

import it.astromark.authentication.dto.UserFirstLoginRequest;
import it.astromark.authentication.dto.UserLoginRequest;
import it.astromark.user.commons.model.SchoolUser;
import it.astromark.user.parent.entity.Parent;
import it.astromark.user.secretary.entity.Secretary;
import it.astromark.user.student.entity.Student;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public interface AuthenticationService {

    SchoolUser login(UserLoginRequest user);

    SchoolUser getUser(UUID id, String role);

    String verify(String username, String password, String schoolCode, String role);

    SchoolUser firstLogin(@NotNull UserFirstLoginRequest user);

    GrantedAuthority getRole(SchoolUser user);

    boolean isStudent();

    boolean isTeacher();

    boolean isParent();

    boolean isSecretary();

    Optional<Parent> getParent();

    Optional<Student> getStudent();

    Optional<Teacher> getTeacher();

    Optional<Secretary> getSecretary();

}
