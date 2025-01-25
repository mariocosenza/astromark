package it.astromark.classmanagement.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.classmanagement.didactic.repository.TeachingRepository;
import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.classmanagement.dto.SchoolClassStudentResponse;
import it.astromark.classmanagement.dto.TeachingResponse;
import it.astromark.classmanagement.mapper.ClassManagementMapper;
import it.astromark.classmanagement.repository.SchoolClassRepository;
import it.astromark.classmanagement.repository.TeacherClassRepository;
import it.astromark.user.commons.model.SchoolUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.Comparator;
import java.util.List;

@Service
public class ClassManagementServiceImpl implements ClassManagementService {

    private final AuthenticationService authenticationService;
    private final ClassManagementMapper classManagementMapper;
    private final TeacherClassRepository teacherClassRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final TeachingRepository teachingRepository;

    public ClassManagementServiceImpl(AuthenticationService authenticationService, ClassManagementMapper classManagementMapper, TeacherClassRepository teacherClassRepository, SchoolClassRepository schoolClassRepository, TeachingRepository teachingRepository) {
        this.authenticationService = authenticationService;
        this.classManagementMapper = classManagementMapper;
        this.teacherClassRepository = teacherClassRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.teachingRepository = teachingRepository;
    }

    @Override
    public Year getYear() {
        var month = LocalDate.now().getMonthValue();
        if (month <= 9) {
            return Year.of(LocalDate.now().getYear());
        } else {
            return Year.of(LocalDate.now().getYear() - 1);
        }
    }

    @Override
    @PreAuthorize("hasRole('SECRETARY') || hasRole('TEACHER')")
    @Transactional
    public List<SchoolClassResponse> getClasses() {
        SchoolUser user;
        if (authenticationService.getSecretary().isPresent()) {
            user = authenticationService.getSecretary().get();
        } else {
            user = authenticationService.getTeacher().orElseThrow();
        }

        return classManagementMapper.toSchoolClassResponseList(user.getSchool().getSchoolClasses());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('SECRETARY') || hasRole('TEACHER')")
    public List<SchoolClassStudentResponse> getStudents(Integer classId) {
        var teacher = authenticationService.getTeacher().orElseThrow();
        if (teacherClassRepository.findByTeacher(teacher).stream()
                .noneMatch(c -> c.getSchoolClass().getId().equals(classId))) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        var students = schoolClassRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found with ID: " + classId))
                .getStudents()
                .stream()
                .sorted(Comparator.comparing(SchoolUser::getSurname))
                .toList();


        return classManagementMapper.toSchoolClassStudentResponseList(students);
    }

    @Override
    public List<TeachingResponse> getTeachings() {
        return teachingRepository.findAll().stream()
                .map(t -> new TeachingResponse(
                        t.getTeacher().getName(),
                        t.getTeacher().getSurname(),
                        t.getSubjectTitle().getTitle()
                ))
                .toList();
    }

}
