package it.astromark.rating.serivice;


import it.astromark.authentication.service.AuthenticationService;
import it.astromark.rating.dto.MarkResponse;
import it.astromark.rating.dto.SemesterReportResponse;
import it.astromark.rating.mapper.MarkMapper;
import it.astromark.rating.model.Mark;
import it.astromark.rating.repository.MarkRepository;
import it.astromark.rating.repository.SemesterReportRepository;
import it.astromark.user.commons.service.SchoolUserService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    private final SchoolUserService schoolUserService;
    private final SemesterReportRepository semesterReportRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public MarkServiceImpl(MarkRepository markRepository, MarkMapper markMapper, SchoolUserService schoolUserService, SemesterReportRepository semesterReportRepository, AuthenticationService authenticationService) {
        this.markRepository = markRepository;
        this.markMapper = markMapper;
        this.schoolUserService = schoolUserService;
        this.semesterReportRepository = semesterReportRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    @PreAuthorize("hasRole('student') || hasRole('parent')")
    public List<MarkResponse> getMarkByYear(UUID studentId, Year year) {
        if(!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        return markMapper.toMarkResponseList(markRepository.findMarkByStudentIdAndDateBetween(studentId, LocalDate.of(year.getValue(), Month.SEPTEMBER, 1),
                LocalDate.of(year.getValue() + 1, Month.AUGUST, 31)));
    }

    @Override
    @PreAuthorize("hasRole('student') || hasRole('parent')")
    public Double getAverage(UUID studentId, Year year) {
        return getMarkByYear(studentId, year).stream()
                .mapToDouble(MarkResponse::mark)
                .average()
                .orElse(0.0);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('student') || hasRole('parent') || hasRole('teacher')")
    public SemesterReportResponse getReport(@NotNull UUID studentId, @PositiveOrZero Short year, Boolean semester) {
        if(!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }  else if (!schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if (!schoolUserService.isLoggedTeacherStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        var report = semesterReportRepository.findByStudent_IdAndFirstSemesterAndYear(studentId, semester, year);
        if(report.isEmpty()) {
            return null;
        }
        else if(!report.getFirst().getPublicField() && authenticationService.isStudent()){
            throw new AccessDeniedException("You are not allowed to access this resource") {
            };
        }


        return markMapper.toSemesterReportResponse(report.getFirst());

    }

    @Override
    @PreAuthorize("hasRole('parent')")
    public SemesterReportResponse viewReport(Integer reportId) {
        var report = semesterReportRepository.findById(reportId).orElseThrow();

        if(!schoolUserService.isLoggedUserParent(report.getStudent().getId())) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        report.setViewed(true);
        semesterReportRepository.save(report);

        return markMapper.toSemesterReportResponse(report);
    }

    @Override
    public MarkResponse create(Mark mark) {
        return null;
    }

    @Override
    public MarkResponse update(Integer integer, Mark mark) {
        return null;
    }

    @Override
    public boolean delete(Integer integer) {
            return false;
    }

    @Override
    public Mark getById(Integer integer) {
        return null;
    }
}
