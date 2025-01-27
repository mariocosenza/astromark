package it.astromark.user.student.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.authentication.utils.PasswordUtils;
import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.classmanagement.entity.SchoolClass;
import it.astromark.classmanagement.mapper.ClassManagementMapper;
import it.astromark.commons.exception.GlobalExceptionHandler;
import it.astromark.commons.service.SendGridMailService;
import it.astromark.orientation.OrientationService;
import it.astromark.school.repository.SchoolRepository;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.mapper.SchoolUserMapper;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.dto.StudentRequest;
import it.astromark.user.student.entity.Student;
import it.astromark.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;
import java.util.function.Supplier;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    private final SchoolUserService schoolUserService;
    private final StudentRepository studentRepository;
    private final ClassManagementMapper classManagementMapper;
    private final AuthenticationService authenticationService;
    private final SendGridMailService sendGridMailService;
    private final SchoolUserMapper schoolUserMapper;
    private final OrientationService orientationService;
    private final SchoolRepository schoolRepository;

    @Autowired
    public StudentServiceImpl(SchoolUserService schoolUserService, StudentRepository studentRepository, ClassManagementMapper classManagementMapper, AuthenticationService authenticationService, SendGridMailService sendGridMailService, SchoolUserMapper schoolUserMapper, OrientationService orientationService, SchoolRepository schoolRepository) {
        this.schoolUserService = schoolUserService;
        this.studentRepository = studentRepository;
        this.classManagementMapper = classManagementMapper;
        this.authenticationService = authenticationService;
        this.sendGridMailService = sendGridMailService;
        this.schoolUserMapper = schoolUserMapper;
        this.orientationService = orientationService;
        this.schoolRepository = schoolRepository;
    }

    @Override
    @PreAuthorize("hasRole('SECRETARY')")
    @Transactional
    public SchoolUserDetailed create(@NotNull StudentRequest studentRequest) {
        var username = studentRequest.name() + "." + studentRequest.surname() + studentRepository.countByNameAndSurname(studentRequest.name(), studentRequest.surname());
        var school = schoolRepository.findBySecretariesContains(Set.of(authenticationService.getSecretary().orElseThrow()));
        var schoolClass = school.getSchoolClasses().stream().filter(c -> c.getId().equals(studentRequest.classId())).findFirst().orElseThrow();
        var password = new Faker().internet().password(8, 64, true, false, true);
        var user = schoolUserMapper.toSchoolUserDetailed(studentRepository.save(Student.builder().school(school)
                .name(studentRequest.name())
                .surname(studentRequest.surname())
                .email(studentRequest.email())
                .username(username)
                .male(studentRequest.male())
                .residentialAddress(studentRequest.residentialAddress())
                .birthDate(studentRequest.birthDate())
                .taxId(studentRequest.taxId())
                .schoolClasses(new HashSet<>(List.of(schoolClass)))
                .pendingState(PendingState.FIRST_LOGIN)
                .password(PasswordUtils.hashPassword(password))
                .build()));
        sendGridMailService.sendAccessMail(school.getCode(), username, studentRequest.email(), password);
        return user;
    }

    @Override
    @PreAuthorize("hasRole('SECRETARY') || hasRole('PARENT') || hasRole('STUDENT')")
    public SchoolUserDetailed getById(UUID uuid) {
        var student = studentRepository.findById(uuid).orElse(null);
        if (!schoolUserService.isLoggedUserParent(uuid)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        } else if (authenticationService.isStudent()) {
            student = authenticationService.getStudent().orElseThrow();
        } else if (authenticationService.isSecretary()) {
            if (!authenticationService.getSecretary().orElseThrow().getSchool().getCode().equals(Objects.requireNonNull(student).getSchool().getCode())) {
                throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
            }
        }
        return schoolUserMapper.toSchoolUserDetailed(student);
    }

    @PreAuthorize("hasRole('STUDENT') || hasRole('PARENT')")
    @Transactional
    public List<Integer> getStudentYears(@NotNull UUID studentId) {
        if (!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        } else if (!schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        }
        Supplier<DataAccessException> exception = () -> new DataAccessException(GlobalExceptionHandler.AUTHORIZATION_DENIED) {
        };
        return studentRepository.findById(studentId).orElseThrow(exception).getSchoolClasses().stream().map(SchoolClass::getYear).toList();
    }

    @Override
    @PreAuthorize("hasRole('STUDENT') || hasRole('PARENT')")
    @Transactional
    public List<SchoolClassResponse> getSchoolClassByYear(@NotNull UUID studentId, @PastOrPresent Year year) {
        if (!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        } else if (!schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        }
        Supplier<DataAccessException> exception = () -> new DataAccessException(GlobalExceptionHandler.AUTHORIZATION_DENIED) {
        };
        return classManagementMapper.toSchoolClassResponseList(studentRepository.findById(studentId).orElseThrow(exception).getSchoolClasses().stream()
                .filter(c -> c.getYear() == year.getValue()).toList());
    }


    @Override
    @PreAuthorize("hasRole('STUDENT') || hasRole('PARENT')")
    public String attitude(UUID studentId) {
        var student = studentRepository.findById(studentId).orElseThrow();
        if (!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        } else if (authenticationService.isStudent()) {
            student = authenticationService.getStudent().orElseThrow();
        }

        try {
            var attitude = orientationService.attitude(student);
            student.setAttitude(attitude);
            studentRepository.save(student);
        } catch (Exception e) {
            log.info("Error while calculating attitude for student with ID: {}", studentId);
        }

        return student.getAttitude();
    }
}
