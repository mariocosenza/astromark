package it.astromark.user.teacher.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.authentication.utils.PasswordUtils;
import it.astromark.commons.service.SendGridMailService;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.mapper.SchoolUserMapper;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.teacher.dto.TeacherRequest;
import it.astromark.user.teacher.entity.Teacher;
import it.astromark.user.teacher.repository.TeacherRepository;
import net.datafaker.Faker;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final SchoolUserMapper schoolUserMapper;
    private final AuthenticationService authenticationService;
    private final TeacherRepository teacherRepository;
    private final SendGridMailService sendGridMailService;

    public TeacherServiceImpl(SchoolUserMapper schoolUserMapper, AuthenticationService authenticationService, TeacherRepository teacherRepository, SendGridMailService sendGridMailService) {
        this.schoolUserMapper = schoolUserMapper;
        this.authenticationService = authenticationService;
        this.teacherRepository = teacherRepository;
        this.sendGridMailService = sendGridMailService;
    }

    @Override
    @PreAuthorize("hasRole('secretary')")
    public SchoolUserDetailed create(TeacherRequest teacherRequest) {
        var username = teacherRequest.name() + "." + teacherRequest.surname() + teacherRepository.countByNameAndSurname(teacherRequest.name(), teacherRequest.surname());
        var school = authenticationService.getSecretary().orElseThrow().getSchool();
        var password = new Faker().internet().password(8, 64, true, false, true);
        var user = schoolUserMapper.toSchoolUserDetailed(teacherRepository.save(Teacher.builder().school(school)
                .name(teacherRequest.name())
                .surname(teacherRequest.surname())
                .email(teacherRequest.email())
                .username(username)
                .gender(teacherRequest.gender())
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
