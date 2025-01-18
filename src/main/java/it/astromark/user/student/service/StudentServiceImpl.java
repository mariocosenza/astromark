package it.astromark.user.student.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.authentication.utils.PasswordUtils;
import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.classmanagement.entity.SchoolClass;
import it.astromark.classmanagement.mapper.ClassManagementMapper;
import it.astromark.commons.service.SendGridMailService;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.mapper.SchoolUserMapper;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.dto.StudentRequest;
import it.astromark.user.student.entity.Student;
import it.astromark.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
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

    @Autowired
    public StudentServiceImpl(SchoolUserService schoolUserService, StudentRepository studentRepository, ClassManagementMapper classManagementMapper, AuthenticationService authenticationService, SendGridMailService sendGridMailService, SchoolUserMapper schoolUserMapper) {
        this.schoolUserService = schoolUserService;
        this.studentRepository = studentRepository;
        this.classManagementMapper = classManagementMapper;
        this.authenticationService = authenticationService;
        this.sendGridMailService = sendGridMailService;
        this.schoolUserMapper = schoolUserMapper;
    }

    @Override
    @PreAuthorize("hasRole('secretary')")
    public SchoolUserDetailed create(StudentRequest studentRequest) {
        var username = studentRequest.name() + "." + studentRequest.surname() + studentRepository.countByNameAndSurname(studentRequest.name(), studentRequest.surname());
        var school = authenticationService.getSecretary().orElseThrow().getSchool();
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
    public SchoolUserDetailed update(UUID uuid, StudentRequest student) {
        return null;
    }

    @Override
    public boolean delete(UUID uuid) {
        return false;
    }

    @Override
    public Student getById(UUID uuid) {
        return studentRepository.findById(uuid).orElse(null);
    }

    @PreAuthorize("hasRole('student') || hasRole('parent')")
    @Transactional
    public List<Integer> getStudentYears(@NotNull UUID studentId) {
        if (!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if (!schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        Supplier<DataAccessException> exception = () -> new DataAccessException("You are not allowed to access this resource") {
        };
        return studentRepository.findById(studentId).orElseThrow(exception).getSchoolClasses().stream().map(SchoolClass::getYear).toList();
    }

    @Override
    @PreAuthorize("hasRole('student') || hasRole('parent')")
    @Transactional
    public List<SchoolClassResponse> getSchoolClassByYear(UUID studentId, Year year) {
        if (!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if (!schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        Supplier<DataAccessException> exception = () -> new DataAccessException("You are not allowed to access this resource") {
        };
        return classManagementMapper.toSchoolClassResponseList(studentRepository.findById(studentId).orElseThrow(exception).getSchoolClasses().stream()
                .filter(c -> c.getYear() == year.getValue()).toList());
    }


}
