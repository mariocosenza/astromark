package it.astromark.user.commons.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.authentication.utils.PasswordUtils;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.dto.SchoolUserResponse;
import it.astromark.user.commons.dto.SchoolUserUpdate;
import it.astromark.user.commons.mapper.SchoolUserMapper;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class SchoolUserServiceImpl implements SchoolUserService {

    private final AuthenticationService authenticationService;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final SchoolUserMapper schoolUserMapper;
    private final SecretaryRepository secretaryRepository;
    private final ParentRepository parentRepository;

    @Autowired
    public SchoolUserServiceImpl(AuthenticationService authenticationService, StudentRepository studentRepository, TeacherRepository teacherRepository, SchoolUserMapper schoolUserMapper, SecretaryRepository secretaryRepository, ParentRepository parentRepository) {
        this.authenticationService = authenticationService;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.schoolUserMapper = schoolUserMapper;
        this.secretaryRepository = secretaryRepository;
        this.parentRepository = parentRepository;
    }

    @Override
    public boolean isStudentParent(Parent parent, UUID studentId) {
        return parent.getStudents().stream().noneMatch(s -> s.getId().equals(studentId));
    }

    @Override
    public boolean isLoggedUserParent(UUID studentId) {
        return !authenticationService.isParent() || isStudentParent(authenticationService.getParent().orElseThrow(), studentId);
    }

    @Override
    public boolean isTeacherClass(Teacher teacher, Integer classId) {
        return teacher.getTeacherClasses().stream().anyMatch(c -> Objects.equals(c.getSchoolClass().getId(), classId));
    }

    @Override
    public boolean isLoggedTeacherClass(Integer classId) {
        return !authenticationService.isTeacher() || isTeacherClass(authenticationService.getTeacher().orElseThrow(), classId);
    }

    @Override
    public boolean isLoggedParentStudentClass(Integer classId) {
        if(authenticationService.isParent()) {
            return authenticationService.getParent().orElseThrow().getStudents().stream().anyMatch(s -> s.getSchoolClasses().stream().anyMatch(c -> Objects.equals(c.getId(), classId)));
        } else {
            return true;
        }
    }

    @Override
    public boolean isLoggedTeacherStudent(UUID studentId) {
        return !authenticationService.isTeacher() || authenticationService.getTeacher().orElseThrow().getTeacherClasses().stream().anyMatch(c -> c.getSchoolClass().getStudents().stream().anyMatch(s -> s.getId().equals(studentId)));
    }

    @Override
    @PreAuthorize("hasRole('student') || hasRole('parent') || hasRole('teacher') || hasRole('secretary')")
    public SchoolUserResponse updatePreferences(SchoolUserUpdate schoolUserUpdate) {
        SchoolUser user;
        if(authenticationService.isStudent()) {
            user = authenticationService.getStudent().orElseThrow();
            user.setPassword(PasswordUtils.hashPassword(schoolUserUpdate.password()));
            studentRepository.save((Student) user);
        } else if(authenticationService.isParent()) {
            user = authenticationService.getParent().orElseThrow();
            user.setPassword(PasswordUtils.hashPassword(schoolUserUpdate.password()));
            parentRepository.save((Parent) user);
        } else if(authenticationService.isTeacher()) {
            user = authenticationService.getTeacher().orElseThrow();
            user.setPassword(PasswordUtils.hashPassword(schoolUserUpdate.password()));
            teacherRepository.save((Teacher) user);
        } else {
            user = authenticationService.getSecretary().orElseThrow();
            user.setPassword(PasswordUtils.hashPassword(schoolUserUpdate.password()));
            secretaryRepository.save((Secretary) user);
        }

        return schoolUserMapper.toSchoolUserResponse(user);
    }

    @Override
    public SchoolUserResponse updateAddress(String address) {
        if(address.length() < 5 || !address.matches("^[a-zA-Z0-9\\s,.'\\-/]{3,100}$")) {
            throw new IllegalArgumentException("Address must be at least 5 characters long");
        }
        SchoolUser user;
        if(authenticationService.isStudent()) {
            user = authenticationService.getStudent().orElseThrow();
            user.setResidentialAddress(address);
            studentRepository.save((Student) user);
        } else if(authenticationService.isParent()) {
            user = authenticationService.getParent().orElseThrow();
            user.setResidentialAddress(address);
            parentRepository.save((Parent) user);
        } else if(authenticationService.isTeacher()) {
            user = authenticationService.getTeacher().orElseThrow();
            user.setResidentialAddress(address);
            teacherRepository.save((Teacher) user);
        } else {
            user = authenticationService.getSecretary().orElseThrow();
            user.setResidentialAddress(address);
            secretaryRepository.save((Secretary) user);
        }

        return schoolUserMapper.toSchoolUserResponse(user);
    }

    @Override
    public boolean isLoggedStudent(UUID studentId) {
        return authenticationService.isStudent() || !authenticationService.getStudent().orElseThrow().getId().equals(studentId);
    }

    @Override
    public SchoolUserDetailed getByIdDetailed() {
        SchoolUser user;
        if(authenticationService.isStudent()) {
            user = authenticationService.getStudent().orElseThrow();
        } else if(authenticationService.isParent()) {
            user = authenticationService.getParent().orElseThrow();
        } else if(authenticationService.isTeacher()) {
            user = authenticationService.getTeacher().orElseThrow();
        } else {
            user = authenticationService.getSecretary().orElseThrow();
        }

        return schoolUserMapper.toSchoolUserDetailed(user);
    }
}
