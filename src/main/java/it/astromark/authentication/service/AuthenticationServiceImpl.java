package it.astromark.authentication.service;

import it.astromark.school.entity.School;
import it.astromark.user.commons.model.SchoolUser;
import it.astromark.user.parent.repository.ParentRepository;
import it.astromark.user.secretary.repository.SecretaryRepository;
import it.astromark.user.student.repository.StudentRepository;
import it.astromark.user.teacher.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

        SchoolUser schoolUser;

        if ((schoolUser = parentRepository.findByUsernameAndPasswordAndAndSchoolCode(username, password, schoolCode)) != null)
            return schoolUser;

        else if ((schoolUser = secretaryRepository.findByUsernameAndPasswordAndAndSchoolCode(username, password, schoolCode)) != null)
            return schoolUser;

        else if ((schoolUser = studentRepository.findByUsernameAndPasswordAndAndSchoolCode(username, password, schoolCode)) != null)
            return schoolUser;

        else if ((schoolUser = teacherRepository.findByUsernameAndPasswordAndAndSchoolCode(username, password, schoolCode)) != null)
            return schoolUser;

        return null;
    }
}
