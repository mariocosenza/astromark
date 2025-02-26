package it.astromark.user.teacher.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.authentication.utils.PasswordUtils;
import it.astromark.classmanagement.didactic.entity.Subject;
import it.astromark.classmanagement.didactic.entity.Teaching;
import it.astromark.classmanagement.didactic.repository.TeachingRepository;
import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.classmanagement.entity.SchoolClass;
import it.astromark.classmanagement.entity.TeacherClass;
import it.astromark.classmanagement.mapper.ClassManagementMapper;
import it.astromark.classmanagement.repository.TeacherClassRepository;
import it.astromark.commons.service.SendGridMailService;
import it.astromark.school.repository.SchoolRepository;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.mapper.SchoolUserMapper;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.teacher.dto.TeacherDetailsResponse;
import it.astromark.user.teacher.dto.TeacherRequest;
import it.astromark.user.teacher.dto.TeacherResponse;
import it.astromark.user.teacher.entity.Teacher;
import it.astromark.user.teacher.repository.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final TeacherClassRepository teacherClassRepository;
    private final SchoolUserMapper schoolUserMapper;
    private final AuthenticationService authenticationService;
    private final TeacherRepository teacherRepository;
    private final SendGridMailService sendGridMailService;
    private final ClassManagementMapper classManagementMapper;
    private final TeachingRepository teachingRepository;
    private final SchoolRepository schoolRepository;

    public TeacherServiceImpl(TeacherClassRepository teacherClassRepository, SchoolUserMapper schoolUserMapper, AuthenticationService authenticationService, TeacherRepository teacherRepository, SendGridMailService sendGridMailService, ClassManagementMapper classManagementMapper, TeachingRepository teachingRepository, SchoolRepository schoolRepository) {
        this.teacherClassRepository = teacherClassRepository;
        this.schoolUserMapper = schoolUserMapper;
        this.authenticationService = authenticationService;
        this.teacherRepository = teacherRepository;
        this.sendGridMailService = sendGridMailService;
        this.classManagementMapper = classManagementMapper;
        this.teachingRepository = teachingRepository;
        this.schoolRepository = schoolRepository;
    }

    @Override
    @PreAuthorize("hasRole('SECRETARY')")
    public SchoolUserDetailed create(TeacherRequest teacherRequest) {
        var username = teacherRequest.name().toLowerCase() + "." + teacherRequest.surname().toLowerCase() + teacherRepository.countByNameAndSurname(teacherRequest.name(), teacherRequest.surname());
        var school = schoolRepository.findBySecretariesContains(Set.of(authenticationService.getSecretary().orElseThrow()));
        var password = new Faker().internet().password(8, 64, true, false, true);
        var user = schoolUserMapper.toSchoolUserDetailed(teacherRepository.save(Teacher.builder()
                .school(school)
                .name(teacherRequest.name())
                .surname(teacherRequest.surname())
                .email(teacherRequest.email())
                .username(username)
                .male(teacherRequest.male())
                .residentialAddress(teacherRequest.residentialAddress())
                .birthDate(teacherRequest.birthDate())
                .taxId(teacherRequest.taxId())
                .pendingState(PendingState.FIRST_LOGIN)
                .password(PasswordUtils.hashPassword(password))
                .build()));
        sendGridMailService.sendAccessMail(school.getCode(), username, teacherRequest.email(), password);
        return user;
    }

    @Override
    @PreAuthorize("hasRole('TEACHER')")
    @Transactional
    public List<SchoolClassResponse> getSchoolClasses() {

        var schoolClasses = teacherClassRepository.findByTeacher(
                        authenticationService.getTeacher().orElseThrow()).stream()
                .map(TeacherClass::getSchoolClass)
                .toList();

        if (schoolClasses.isEmpty()) {
            return null;
        }

        int schoolClassesMaxYear = schoolClasses.stream()
                .max(Comparator.comparingInt(SchoolClass::getYear))
                .get().getYear();

        return classManagementMapper.toSchoolClassResponseList(schoolClasses.stream()
                .filter(c -> c.getYear() == schoolClassesMaxYear)
                .sorted(Comparator.comparing(SchoolClass::getLetter)
                        .thenComparing(SchoolClass::getNumber))
                .toList()
        );
    }

    @Override
    public List<String> getTeaching() {
        var teachings = teachingRepository.findByTeacher(authenticationService.getTeacher().orElseThrow());
        return teachings.stream().map(Teaching::getSubjectTitle).map(Subject::getTitle).sorted().toList();
    }

    @Override
    public List<TeacherResponse> getTeachers() {
        return teacherRepository.findAll().stream().map(
                t -> new TeacherResponse(
                        t.getName(),
                        t.getSurname(),
                        t.getId()
                )
        ).toList();
    }

    @Override
    @Transactional
    public TeacherDetailsResponse getTeacherTeaching(@NotEmpty String teacheruuid) {
        var teacher = teacherRepository.findById(UUID.fromString(teacheruuid))
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found for UUID: " + teacheruuid));

        List<String> teachings = teacher.getTeachings().stream()
                .map(teaching -> teaching.getSubjectTitle().getTitle() + " - " + teaching.getTypeOfActivity())
                .toList();

        return new TeacherDetailsResponse(teacher.getName() + ' ' + teacher.getSurname(), teachings);
    }


    @Override
    public SchoolUserDetailed update(UUID uuid, TeacherRequest teacherRequest) {
        return null;
    }

    @Override
    public boolean delete(UUID uuid) {
        return false;
    }

    @Override
    public Teacher getById(UUID uuid) {
        return null;
    }
}

