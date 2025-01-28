package it.astromark.authentication.service;

import it.astromark.authentication.dto.UserFirstLoginRequest;
import it.astromark.authentication.dto.UserLoginRequest;
import it.astromark.authentication.utils.PasswordUtils;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.commons.model.Role;
import it.astromark.user.commons.model.SchoolUser;
import it.astromark.user.parent.entity.Parent;
import it.astromark.user.parent.repository.ParentRepository;
import it.astromark.user.secretary.entity.Secretary;
import it.astromark.user.secretary.repository.SecretaryRepository;
import it.astromark.user.student.entity.Student;
import it.astromark.user.student.repository.StudentRepository;
import it.astromark.user.teacher.entity.Teacher;
import it.astromark.user.teacher.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ParentRepository parentRepository;
    private final SecretaryRepository secretaryRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final JWTService jwtService;

    @Autowired
    public AuthenticationServiceImpl(ParentRepository parentRepository, SecretaryRepository secretaryRepository, StudentRepository studentRepository, TeacherRepository teacherRepository, JWTService jwtService) {
        this.parentRepository = parentRepository;
        this.secretaryRepository = secretaryRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.jwtService = jwtService;
    }

    @Override
    public SchoolUser login(UserLoginRequest user) {
        var schoolUser = findUserInRepositories(user.username(), user.schoolCode(), user.role());
        if (schoolUser == null) {
            return null;
        }

        if (user.username().isBlank() || user.username().length() < 5 || user.username().length() > 256 ||
                !user.schoolCode().matches("^SS\\d{5}$") ||
                user.role().isBlank()) {
            throw new IllegalArgumentException("Invalid input");
        }

        if(schoolUser.getPendingState() == PendingState.REMOVE) {
            log.info("User is pending removal");
            return null;
        } else if (schoolUser.getPendingState() == PendingState.NORMAL && !user.password().matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")) {
            throw new IllegalArgumentException("Invalid input");
        }

        var hashedPassword = PasswordUtils.hashPassword(user.password());
        if (hashedPassword.equals(schoolUser.getPassword()))
            return schoolUser;

        log.info("User not found in any repository");
        return null;
    }

    @Override
    public SchoolUser firstLogin(UserFirstLoginRequest user) {
        var schoolUser = findUserInRepositories(user.username(), user.schoolCode(), user.role());
        if(!user.password().matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")) {
            throw new IllegalArgumentException("Invalid input");
        }

        var hashedPassword = PasswordUtils.hashPassword(user.password());
        if (schoolUser != null && hashedPassword.equals(schoolUser.getPassword())) {
            schoolUser.setPassword(PasswordUtils.hashPassword(user.newPassword()));
            schoolUser.setPendingState(PendingState.NORMAL);
            SchoolUser updatedUser = switch (user.role().toLowerCase()) {
                case "student" -> studentRepository.save((Student) schoolUser);
                case "teacher" -> teacherRepository.save((Teacher) schoolUser);
                case "parent" -> parentRepository.save((Parent) schoolUser);
                case "secretary" -> secretaryRepository.save((Secretary) schoolUser);
                default -> null;
            };
            if (updatedUser != null) {
                return login(new UserLoginRequest(updatedUser.getUsername(), user.newPassword(), user.schoolCode(), user.role()));
            }
        }

        return null;
    }

    @Override
    @Transactional
    public SchoolUser getUser(UUID id, String role) {
        Supplier<DataAccessException> exceptionSupplier = () -> new DataAccessException("UUID not found in any repository: " + id) {
        };

        return switch (role) {
            case "ROLE_STUDENT" -> studentRepository.findById(id).orElseThrow(exceptionSupplier);
            case "ROLE_TEACHER" -> teacherRepository.findById(id).orElseThrow(exceptionSupplier);
            case "ROLE_PARENT" -> parentRepository.findById(id).orElseThrow(exceptionSupplier);
            case "ROLE_SECRETARY" -> secretaryRepository.findById(id).orElseThrow(exceptionSupplier);
            default -> throw exceptionSupplier.get();
        };
    }

    @Override
    public String verify(String username, String password, String schoolCode, String role) {

        var schoolUser = login(new UserLoginRequest(username, password, schoolCode, role));
        if (schoolUser != null)
            return jwtService.generateToken(schoolUser.getId(), getRole(schoolUser));
        else return null;
    }


    @Override
    public GrantedAuthority getRole(SchoolUser user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        return switch (user) {
            case Parent ignored -> new SimpleGrantedAuthority("ROLE_" + Role.PARENT.toString().toUpperCase());
            case Teacher ignored -> new SimpleGrantedAuthority("ROLE_" + Role.TEACHER.toString().toUpperCase());
            case Student ignored -> new SimpleGrantedAuthority("ROLE_" + Role.STUDENT.toString().toUpperCase());
            case Secretary ignored -> new SimpleGrantedAuthority("ROLE_" + Role.SECRETARY.toString().toUpperCase());
            default ->
                    throw new IllegalStateException("Unexpected user type: " + user.getClass().toString().toLowerCase());
        };

    }


    public SchoolUser findUserInRepositories(String username, String schoolCode, String role) {
        // Cerca l'utente in ciascun repository
        return switch (role.toLowerCase()) {
            case "student" -> studentRepository.findByUsernameAndSchoolCode(username, schoolCode);
            case "teacher" -> teacherRepository.findByUsernameAndSchoolCode(username, schoolCode);
            case "parent" -> parentRepository.findByUsernameAndSchoolCode(username, schoolCode);
            case "secretary" -> secretaryRepository.findByUsernameAndSchoolCode(username, schoolCode);
            default -> null;
        }; // Nessun utente trovato
    }

    public boolean isStudent() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Student;
    }

    public boolean isTeacher() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Teacher;
    }

    public boolean isParent() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Parent;
    }

    public boolean isSecretary() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Secretary;
    }

    public Optional<Parent> getParent() {
        return isParent() ? Optional.of((Parent) SecurityContextHolder.getContext().getAuthentication().getPrincipal()) : Optional.empty();
    }

    public Optional<Student> getStudent() {
        return isStudent() ? Optional.of((Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal()) : Optional.empty();
    }

    public Optional<Teacher> getTeacher() {
        return isTeacher() ? Optional.of((Teacher) SecurityContextHolder.getContext().getAuthentication().getPrincipal()) : Optional.empty();
    }

    public Optional<Secretary> getSecretary() {
        return isSecretary() ? Optional.of((Secretary) SecurityContextHolder.getContext().getAuthentication().getPrincipal()) : Optional.empty();
    }
}
