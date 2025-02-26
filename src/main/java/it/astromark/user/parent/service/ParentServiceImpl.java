package it.astromark.user.parent.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.authentication.utils.PasswordUtils;
import it.astromark.classmanagement.entity.TeacherClass;
import it.astromark.commons.service.SendGridMailService;
import it.astromark.school.repository.SchoolRepository;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.dto.SchoolUserResponse;
import it.astromark.user.commons.mapper.SchoolUserMapper;
import it.astromark.user.commons.model.SchoolUser;
import it.astromark.user.parent.dto.ParentDetailedResponse;
import it.astromark.user.parent.dto.ParentRequest;
import it.astromark.user.parent.entity.Parent;
import it.astromark.user.parent.repository.ParentRepository;
import it.astromark.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ParentServiceImpl implements ParentService {


    private final SendGridMailService sendGridMailService;
    private final ParentRepository parentRepository;
    private final AuthenticationService authenticationService;
    private final SchoolUserMapper schoolUserMapper;
    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;

    public ParentServiceImpl(SendGridMailService sendGridMailService, ParentRepository parentRepository, AuthenticationService authenticationService, SchoolUserMapper schoolUserMapper, StudentRepository studentRepository, SchoolRepository schoolRepository) {
        this.sendGridMailService = sendGridMailService;
        this.parentRepository = parentRepository;
        this.authenticationService = authenticationService;
        this.schoolUserMapper = schoolUserMapper;
        this.studentRepository = studentRepository;
        this.schoolRepository = schoolRepository;
    }

    @Override
    @PreAuthorize("hasRole('SECRETARY')")
    public ParentDetailedResponse create(ParentRequest parentRequest) {
        var username = parentRequest.name().toLowerCase() + "." + parentRequest.surname().toLowerCase() + parentRepository.countByNameAndSurname(parentRequest.name(), parentRequest.surname());
        var school = schoolRepository.findBySecretariesContains(Set.of(authenticationService.getSecretary().orElseThrow()));
        var student = studentRepository.findById(parentRequest.studentId()).orElseThrow();
        var password = new Faker().internet().password(8, 64, true, false, true);
        var user = schoolUserMapper.toParentDetailedResponse(parentRepository.save(Parent.builder().school(school)
                .name(parentRequest.name())
                .surname(parentRequest.surname())
                .email(parentRequest.email())
                .username(username)
                .male(parentRequest.male())
                .residentialAddress(parentRequest.residentialAddress())
                .birthDate(parentRequest.birthDate())
                .taxId(parentRequest.taxId())
                .legalGuardian(parentRequest.legalGuardian())
                .password(PasswordUtils.hashPassword(password))
                .student(student)
                .build()));
        sendGridMailService.sendAccessMail(school.getCode(), username, parentRequest.email(), password);
        return user;
    }

    @Override
    public ParentDetailedResponse update(UUID uuid, ParentRequest parentRequest) {
        return null;
    }

    @Override
    public boolean delete(UUID uuid) {
        return false;
    }

    @Override
    public Parent getById(UUID uuid) {
        return null;
    }

    @Override
    @Transactional
    public List<SchoolUserDetailed> getStudents() {
        return schoolUserMapper.toSchoolUserDetailedList(authenticationService.getParent().orElseThrow().getStudents());
    }

    @Override
    @Transactional
    public List<SchoolUserResponse> getTeachers() {
        return schoolUserMapper.toSchoolUserResponseList(authenticationService.getParent().orElseThrow()
                .getStudents().stream()
                .flatMap(student -> student.getSchoolClasses().stream()
                        .flatMap(schoolClass -> schoolClass.getTeacherClasses().stream()
                                .map(TeacherClass::getTeacher))).sorted(Comparator.comparing(SchoolUser::getSurname)).toList());
    }
}
