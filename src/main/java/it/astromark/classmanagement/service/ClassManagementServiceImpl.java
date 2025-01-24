package it.astromark.classmanagement.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.classmanagement.mapper.ClassManagementMapper;
import it.astromark.user.commons.model.SchoolUser;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Service
public class ClassManagementServiceImpl implements ClassManagementService {

    private final AuthenticationService authenticationService;
    private final ClassManagementMapper classManagementMapper;

    public ClassManagementServiceImpl(AuthenticationService authenticationService, ClassManagementMapper classManagementMapper) {
        this.authenticationService = authenticationService;
        this.classManagementMapper = classManagementMapper;
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
}
