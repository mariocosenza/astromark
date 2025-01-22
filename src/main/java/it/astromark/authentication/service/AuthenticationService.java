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

/**
 * Service interface for managing authentication-related operations.
 * Provides methods for user login, logout, and token validation.
 */
@Service
public interface AuthenticationService {

    /**
     * Authenticates a user and allows them to log in.
     *
     * @param user the login request containing user credentials
     * @return a `SchoolUser` object representing the logged-in user
     * Pre-condition: The `user` must not be null and must contain valid credentials.
     * Post-condition: Returns the authenticated user details if the login is successful.
     */
    SchoolUser login(UserLoginRequest user);

    /**
     * Retrieves user details based on their ID and role.
     *
     * @param id the UUID of the user
     * @param role the role of the user (e.g., student, teacher, parent, etc.)
     * @return a `SchoolUser` object representing the user
     * Pre-condition: The `id` must not be null. The `role` must not be null or empty.
     * Post-condition: Returns the user details if the ID and role are valid.
     */
    SchoolUser getUser(UUID id, String role);

    /**
     * Verifies user credentials and returns a verification status.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param schoolCode the school code associated with the user
     * @param role the role of the user
     * @return a verification string indicating the result
     * Pre-condition: The `username`, `password`, `schoolCode`, and `role` must not be null or empty.
     * Post-condition: Returns a verification result indicating whether the credentials are valid.
     */
    String verify(String username, String password, String schoolCode, String role);

    /**
     * Handles the first login process for a new user.
     *
     * @param user the request containing details for the first login
     * @return a `SchoolUser` object representing the user after the first login
     * Pre-condition: The `user` must not be null and must contain valid details for the first login.
     * Post-condition: Returns the user details after the first login is completed.
     */
    SchoolUser firstLogin(@NotNull UserFirstLoginRequest user);

    /**
     * Retrieves the granted authority (role) of a user.
     *
     * @param user the `SchoolUser` object
     * @return the granted authority of the user
     * Pre-condition: The `user` must not be null.
     * Post-condition: Returns the granted authority of the specified user.
     */
    GrantedAuthority getRole(SchoolUser user);

    /**
     * Checks if the current user is a student.
     *
     * @return true if the user is a student, false otherwise
     * Pre-condition: The user must be logged in.
     * Post-condition: Returns true if the user is identified as a student.
     */
    boolean isStudent();

    /**
     * Checks if the current user is a teacher.
     *
     * @return true if the user is a teacher, false otherwise
     * Pre-condition: The user must be logged in.
     * Post-condition: Returns true if the user is identified as a teacher.
     */
    boolean isTeacher();

    /**
     * Checks if the current user is a parent.
     *
     * @return true if the user is a parent, false otherwise
     * Pre-condition: The user must be logged in.
     * Post-condition: Returns true if the user is identified as a parent.
     */
    boolean isParent();

    /**
     * Checks if the current user is a secretary.
     *
     * @return true if the user is a secretary, false otherwise
     * Pre-condition: The user must be logged in.
     * Post-condition: Returns true if the user is identified as a secretary.
     */
    boolean isSecretary();

    /**
     * Retrieves the parent details if the user is a parent.
     *
     * @return an `Optional<Parent>` containing the parent details if available
     * Pre-condition: The user must be logged in and identified as a parent.
     * Post-condition: Returns an optional containing the parent details or empty if not applicable.
     */
    Optional<Parent> getParent();

    /**
     * Retrieves the student details if the user is a student.
     *
     * @return an `Optional<Student>` containing the student details if available
     * Pre-condition: The user must be logged in and identified as a student.
     * Post-condition: Returns an optional containing the student details or empty if not applicable.
     */
    Optional<Student> getStudent();

    /**
     * Retrieves the teacher details if the user is a teacher.
     *
     * @return an `Optional<Teacher>` containing the teacher details if available
     * Pre-condition: The user must be logged in and identified as a teacher.
     * Post-condition: Returns an optional containing the teacher details or empty if not applicable.
     */
    Optional<Teacher> getTeacher();

    /**
     * Retrieves the secretary details if the user is a secretary.
     *
     * @return an `Optional<Secretary>` containing the secretary details if available
     * Pre-condition: The user must be logged in and identified as a secretary.
     * Post-condition: Returns an optional containing the secretary details or empty if not applicable.
     */
    Optional<Secretary> getSecretary();


}
