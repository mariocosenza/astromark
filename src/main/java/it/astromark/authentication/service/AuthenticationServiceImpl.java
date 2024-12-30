package it.astromark.authentication.service;

import it.astromark.school.SchoolRepository;
import it.astromark.school.entity.School;
import it.astromark.user.commons.model.SchoolUser;
import it.astromark.user.parent.repository.ParentRepository;
import it.astromark.user.secretary.repository.SecretaryRepository;
import it.astromark.user.student.repository.StudentRepository;
import it.astromark.user.teacher.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ParentRepository parentRepository;
    private final SecretaryRepository secretaryRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final SchoolRepository schoolRepository;

    @Autowired
    public AuthenticationServiceImpl(ParentRepository parentRepository, SecretaryRepository secretaryRepository, StudentRepository studentRepository, TeacherRepository teacherRepository, SchoolRepository schoolRepository) {
        this.parentRepository = parentRepository;
        this.secretaryRepository = secretaryRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.schoolRepository = schoolRepository;
    }

    @Override
    public SchoolUser login(String username, String password, String schoolCode, String role) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        School school = schoolRepository.findByCode(schoolCode);

        // Cerca l'utente nei vari repository
        SchoolUser schoolUser = findUserInRepositories(username, school, role);

        // Se l'utente esiste e la password corrisponde
        if (schoolUser != null && encoder.matches(password, schoolUser.getPassword())) {
            return schoolUser;
        }

        // Se nessun utente trovato o password non valida
        return null;
    }

    @Override
    public String schoolCode(SchoolUser schoolUser) {
        return schoolUser.getSchoolCode().getCode();
    }

    private SchoolUser findUserInRepositories(String username, School schoolCode, String role) {
        // Cerca l'utente in ciascun repository

        SchoolUser user = null;

        switch (role) {
            case "student":
                user = studentRepository.findByUsernameAndSchoolCode(username, schoolCode);
                break;
            case "teacher":
                user = teacherRepository.findByUsernameAndSchoolCode(username, schoolCode);
                break;
            case "parent":
                user = parentRepository.findByUsernameAndSchoolCode(username, schoolCode);
                break;
            case "secretary":
                user = secretaryRepository.findByUsernameAndSchoolCode(username, schoolCode);
                break;

        }
        return user; // Nessun utente trovato
    }

}
