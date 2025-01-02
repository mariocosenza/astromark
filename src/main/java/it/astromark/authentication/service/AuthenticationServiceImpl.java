package it.astromark.authentication.service;

import it.astromark.authentication.utils.PasswordUtils;
import it.astromark.school.repository.SchoolRepository;
import it.astromark.school.entity.School;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ParentRepository parentRepository;
    private final SecretaryRepository secretaryRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final SchoolRepository schoolRepository;
    private final JWTService jwtService;

    @Autowired
    public AuthenticationServiceImpl(ParentRepository parentRepository, SecretaryRepository secretaryRepository, StudentRepository studentRepository, TeacherRepository teacherRepository, SchoolRepository schoolRepository, JWTService jwtService) {
        this.parentRepository = parentRepository;
        this.secretaryRepository = secretaryRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.schoolRepository = schoolRepository;
        this.jwtService = jwtService;
    }

    @Override
    public SchoolUser login(String username, String password, String schoolCode, String role) {

        var school = schoolRepository.findByCode(schoolCode);
        if (school == null) return null;

        // Cerca l'utente nei vari repository
        var schoolUser = findUserInRepositories(username, school.getCode(), role);

        if (schoolUser == null) return null;


        var hashedPassword = PasswordUtils.hashPassword(password);
        if (hashedPassword.equals(schoolUser.getPassword()))
            return schoolUser;

        // Se nessun utente trovato o password non valida
        return null;
    }

    public String verify(String username, String password, String schoolCode, String role) {

        var schoolUser = login(username, password, schoolCode, role);
        if (schoolUser != null)
            return jwtService.generateToken(schoolUser.getId() , getRole(schoolUser));
        else return null;
    }


    @Override
    public String schoolCode(SchoolUser schoolUser) {
        return schoolUser.getSchool().getCode();
    }

    @Override
    public Role getRole(SchoolUser user) {
        return switch (user) {
            case null -> throw new IllegalArgumentException("User cannot be null");
            case Parent ignored -> Role.PARENT;
            case Teacher ignored -> Role.TEACHER;
            case Student ignored -> Role.STUDENT;
            case Secretary ignored -> Role.SECRETARY;
            default -> throw new IllegalStateException("Unexpected user type: " + user.getClass());
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
