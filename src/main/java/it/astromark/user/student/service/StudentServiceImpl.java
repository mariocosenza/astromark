package it.astromark.user.student.service;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.classmanagement.mapper.ClassManagementMapper;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.dto.StudentRequest;
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
    private final ClassManagementMapper classManagementMapper;

    @Autowired
    public StudentServiceImpl(SchoolUserService schoolUserService, StudentRepository studentRepository, ClassManagementMapper classManagementMapper) {
        this.schoolUserService = schoolUserService;
        this.studentRepository = studentRepository;
        this.classManagementMapper = classManagementMapper;
    }

    @Override
    public Student create(StudentRequest studentRequest) {
        return null;
    }

    @Override
    public Student update(UUID uuid, StudentRequest student) {
        return null;
    }

    @Override
    public Student delete(UUID uuid) {
        return null;
    }

    @Override
    public Student getById(UUID uuid) {
        return studentRepository.findById(uuid).orElse(null);
    }

    @PreAuthorize("hasRole('student') || hasRole('parent')")
    @Transactional
    public List<Year> getStudentYears(UUID studentId) {
        if(!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if(!schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        Supplier<DataAccessException> exception = () -> new DataAccessException("You are not allowed to access this resource") {};
        return studentRepository.findById(studentId).orElseThrow(exception).getSchoolClasses().stream().map(c -> Year.of(c.getYear())).toList();
    }

    @Override
    @PreAuthorize("hasRole('student') || hasRole('parent')")
    @Transactional
    public List<SchoolClassResponse> getSchoolClassByYear(UUID studentId, Year year) {
        if(!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if(!schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        Supplier<DataAccessException> exception = () -> new DataAccessException("You are not allowed to access this resource") {};
        return classManagementMapper.toSchoolClassResponseList(studentRepository.findById(studentId).orElseThrow(exception).getSchoolClasses().stream()
                .filter(c -> c.getYear() == year.getValue()).toList());
    }



}
