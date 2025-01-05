package it.astromark.authentication.service;

import it.astromark.authentication.utils.PasswordUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Supplier;

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
    public SchoolUser login(String username, String password, String schoolCode, String role) {


        // Cerca l'utente nei vari repository
        var schoolUser = findUserInRepositories(username, schoolCode, role);
        if (schoolUser == null) return null;


        var hashedPassword = PasswordUtils.hashPassword(password);
        if (hashedPassword.equals(schoolUser.getPassword()))
            return schoolUser;

        // Se nessun utente trovato o password non valida
        return null;
    }

    @Override
    @Transactional
    public SchoolUser getUser(UUID id, String role) {
        Supplier<RuntimeException> exceptionSupplier = () -> new RuntimeException("UUID not found in any repository: " + id);

        return switch (role) {
            case "ROLE_student" -> studentRepository.findById(id).orElseThrow(exceptionSupplier);
            case "ROLE_teacher" -> teacherRepository.findById(id).orElseThrow(exceptionSupplier);
            case "ROLE_parent" -> parentRepository.findById(id).orElseThrow(exceptionSupplier);
            case "ROLE_secretary" -> secretaryRepository.findById(id).orElseThrow(exceptionSupplier);
            default -> throw exceptionSupplier.get();
        };
    }

    @Override
    public String verify(String username, String password, String schoolCode, String role) {

        var schoolUser = login(username, password, schoolCode, role);
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
            case Parent ignored -> new SimpleGrantedAuthority("ROLE_" + Role.PARENT.toString().toLowerCase());
            case Teacher ignored -> new SimpleGrantedAuthority("ROLE_" + Role.TEACHER.toString().toLowerCase());
            case Student ignored -> new SimpleGrantedAuthority("ROLE_" + Role.STUDENT.toString().toLowerCase());
            case Secretary ignored -> new SimpleGrantedAuthority("ROLE_" + Role.SECRETARY.toString().toLowerCase());
            default ->
                    throw new IllegalStateException("Unexpected user type: " + user.getClass().toString().toLowerCase());
        };

    }


    private SchoolUser findUserInRepositories(String username, String schoolCode, String role) {
        // Cerca l'utente in ciascun repository
        return switch (role.toLowerCase()) {
            case "student" -> studentRepository.findByUsernameAndSchoolCode(username, schoolCode);
            case "teacher" -> teacherRepository.findByUsernameAndSchoolCode(username, schoolCode);
            case "parent" -> parentRepository.findByUsernameAndSchoolCode(username, schoolCode);
            case "secretary" -> secretaryRepository.findByUsernameAndSchoolCode(username, schoolCode);
            default -> null;
        }; // Nessun utente trovato
    }

}
