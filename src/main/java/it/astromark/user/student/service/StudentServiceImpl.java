package it.astromark.user.student.service;

import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.entity.Student;
import it.astromark.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Service
public class StudentServiceImpl implements StudentService{

    private final SchoolUserService schoolUserService;
    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(SchoolUserService schoolUserService, StudentRepository studentRepository) {
        this.schoolUserService = schoolUserService;
        this.studentRepository = studentRepository;
    }

    public Student create(Student r) {
        return null;
    }

    @Override
    public Student update(UUID uuid, Student student) {
        return null;
    }

    @Override
    public Student delete(UUID uuid) {
        return null;
    }

    @Override
    public Student getById(UUID uuid) {
        return null;
    }

    @PreAuthorize("hasRole('student') || hasRole('parent')")
    @Transactional
    public List<Year> getStudentYears(UUID studentId) {
        if(!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        Supplier<DataAccessException> exception = () -> new DataAccessException("You are not allowed to access this resource") {};
        return studentRepository.findById(studentId).orElseThrow(exception).getSchoolClasses().stream().map(c -> Year.of(c.getYear())).toList();
    }
}
