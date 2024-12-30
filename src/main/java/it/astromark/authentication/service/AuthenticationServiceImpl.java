package it.astromark.authentication.service;

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

    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private SecretaryRepository secretaryRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;


    @Override
    public SchoolUser login(String username, String password, School schoolCode) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Cerca l'utente nei vari repository
        SchoolUser schoolUser = findUserInRepositories(username, schoolCode);

        // Se l'utente esiste e la password corrisponde
        if (schoolUser != null && encoder.matches(password, schoolUser.getPassword())) {
            return schoolUser;
        }

        // Se nessun utente trovato o password non valida
        return null;
    }

    private SchoolUser findUserInRepositories(String username, School schoolCode) {
        // Cerca l'utente in ciascun repository
        SchoolUser user = parentRepository.findByUsernameAndSchoolCode(username, schoolCode);
        if (user != null) return user;

        user = secretaryRepository.findByUsernameAndSchoolCode(username, schoolCode);
        if (user != null) return user;

        user = studentRepository.findByUsernameAndSchoolCode(username, schoolCode);
        if (user != null) return user;

        user = teacherRepository.findByUsernameAndSchoolCode(username, schoolCode);
        if (user != null) return user;

        return null; // Nessun utente trovato
    }

}
