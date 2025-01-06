package it.astromark.rating.serivice;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.rating.dto.MarkResponse;
import it.astromark.rating.mapper.MarkMapper;
import it.astromark.rating.repository.MarkRepository;
import it.astromark.user.commons.service.SchoolUserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MarkServiceImpl implements MarkService {

    private final MarkRepository markRepository;
    private final MarkMapper markMapper;
    private final AuthenticationService authenticationService;
    private final SchoolUserService schoolUserService;

    @Autowired
    public MarkServiceImpl(MarkRepository markRepository, MarkMapper markMapper, AuthenticationService authenticationService, SchoolUserService schoolUserService) {
        this.markRepository = markRepository;
        this.markMapper = markMapper;
        this.authenticationService = authenticationService;
        this.schoolUserService = schoolUserService;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('student') || hasRole('teacher')")
    public List<MarkResponse> getMarkByYear(UUID studentId, Year year) {
        if(authenticationService.isParent() && !schoolUserService.isStudentParent(authenticationService.getParent().orElseThrow(), studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        return markMapper.toMarkResponseList(markRepository.findMarkByDateBetween(LocalDate.of(year.getValue(), Month.SEPTEMBER, 1),
                LocalDate.of(year.getValue() + 1, Month.AUGUST, 31)));
    }
}
