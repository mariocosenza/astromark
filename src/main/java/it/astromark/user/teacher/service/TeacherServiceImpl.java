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
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.mapper.SchoolUserMapper;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.teacher.dto.TeacherDetailsResponse;
import it.astromark.user.teacher.dto.TeacherRequest;
import it.astromark.user.teacher.dto.TeacherResponse;
import it.astromark.user.teacher.entity.Teacher;
import it.astromark.user.teacher.repository.TeacherRepository;
import org.springframework.transaction.annotation.Transactional;
import net.datafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository repository;
    private final TeacherClassRepository teacherClassRepository;
    private final SchoolUserMapper schoolUserMapper;
    private final AuthenticationService authenticationService;
    private final TeacherRepository teacherRepository;
    private final SendGridMailService sendGridMailService;
    private final ClassManagementMapper classManagementMapper;
    private final TeachingRepository teachingRepository;

    public TeacherServiceImpl(TeacherRepository repository, TeacherClassRepository teacherClassRepository, SchoolUserMapper schoolUserMapper, AuthenticationService authenticationService, TeacherRepository teacherRepository, SendGridMailService sendGridMailService, ClassManagementMapper classManagementMapper, TeachingRepository teachingRepository) {
        this.repository = repository;
        this.teacherClassRepository = teacherClassRepository;
        this.schoolUserMapper = schoolUserMapper;
        this.authenticationService = authenticationService;
        this.teacherRepository = teacherRepository;
        this.sendGridMailService = sendGridMailService;
        this.classManagementMapper = classManagementMapper;
        this.teachingRepository = teachingRepository;
    }

    @Override
    @PreAuthorize("hasRole('SECRETARY')")
    public SchoolUserDetailed create(TeacherRequest teacherRequest) {
        var username = teacherRequest.name() + "." + teacherRequest.surname() + teacherRepository.countByNameAndSurname(teacherRequest.name(), teacherRequest.surname());
        var school = authenticationService.getSecretary().orElseThrow(() -> new IllegalArgumentException("Secretary not found")).getSchool();
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
    public TeacherDetailsResponse getTeacher(String teacheruuid) {

        var teacher = teacherRepository.findById(UUID.fromString(teacheruuid)).orElseThrow(() ->
                new IllegalArgumentException("Teacher Not Found"));

        var teacherTeachings = teachingRepository.findByTeacher(teacher).stream().map(Teaching::toString).toList();
        return new TeacherDetailsResponse(
                teacher.getUsername(),
                teacherTeachings.toString()

                );
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

