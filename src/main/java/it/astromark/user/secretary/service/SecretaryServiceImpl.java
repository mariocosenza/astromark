package it.astromark.user.secretary.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.authentication.utils.PasswordUtils;
import it.astromark.commons.service.SendGridMailService;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.mapper.SchoolUserMapper;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.secretary.dto.SecretaryRequest;
import it.astromark.user.secretary.entity.Secretary;
import it.astromark.user.secretary.repository.SecretaryRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SecretaryServiceImpl implements SecretaryService{
    private final SecretaryRepository secretaryRepository;
    private final AuthenticationService authenticationService;
    private final SchoolUserMapper schoolUserMapper;
    private final SendGridMailService sendGridMailService;

    @Autowired
    public SecretaryServiceImpl(SecretaryRepository secretaryRepository, AuthenticationService authenticationService, SchoolUserMapper schoolUserMapper, SendGridMailService sendGridMailService) {
        this.secretaryRepository = secretaryRepository;
        this.authenticationService = authenticationService;
        this.schoolUserMapper = schoolUserMapper;
        this.sendGridMailService = sendGridMailService;
    }

    @Override
    @PreAuthorize("hasRole('secretary')")
    public SchoolUserDetailed create(SecretaryRequest secretaryRequest) {
        var username = secretaryRequest.name() + "." + secretaryRequest.surname() + secretaryRepository.countByNameAndSurname(secretaryRequest.name(), secretaryRequest.surname());
        var school = authenticationService.getSecretary().orElseThrow().getSchool();
        var password = new Faker().internet().password(8, 64, true, false, true);
        var user = schoolUserMapper.toSchoolUserDetailed(secretaryRepository.save(Secretary.builder().school(school)
                .name(secretaryRequest.name())
                .surname(secretaryRequest.surname())
                .email(secretaryRequest.email())
                .username(username)
                .gender(secretaryRequest.gender())
                .residentialAddress(secretaryRequest.residentialAddress())
                .birthDate(secretaryRequest.birthDate())
                .taxId(secretaryRequest.taxId())
                .pendingState(PendingState.FIRST_LOGIN)
                .password(PasswordUtils.hashPassword(password))
                .build()));
        sendGridMailService.sendAccessMail(school.getCode(), username, secretaryRequest.email(), password);
        return user;
    }

    @Override
    public SchoolUserDetailed update(UUID uuid, SecretaryRequest secretaryRequest) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    public Secretary getById(UUID uuid) {
        return null;
    }
}
