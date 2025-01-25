package it.astromark.classmanagement.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.classmanagement.dto.SchoolClassStudentResponse;
import it.astromark.classmanagement.mapper.ClassManagementMapper;
import it.astromark.classmanagement.repository.SchoolClassRepository;
import it.astromark.user.commons.model.SchoolUser;
import it.astromark.user.commons.service.SchoolUserService;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.Comparator;
import java.util.List;

@Service
public class ClassManagementServiceImpl implements ClassManagementService {

    private final AuthenticationService authenticationService;
    private final ClassManagementMapper classManagementMapper;
    private final SchoolClassRepository schoolClassRepository;
    private final SchoolUserService schoolUserService;

    public ClassManagementServiceImpl(AuthenticationService authenticationService, ClassManagementMapper classManagementMapper, SchoolClassRepository schoolClassRepository, SchoolUserService schoolUserService) {
        this.authenticationService = authenticationService;
        this.classManagementMapper = classManagementMapper;
        this.schoolClassRepository = schoolClassRepository;
        this.schoolUserService = schoolUserService;
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

        return classManagementMapper.toSchoolClassResponseList(schoolClassRepository.findBySchool(user.getSchool()));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('SECRETARY') || hasRole('TEACHER')")
    public List<SchoolClassStudentResponse> getStudents(Integer classId) {
        if (!schoolUserService.isLoggedTeacherClass(classId)) {
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

}
