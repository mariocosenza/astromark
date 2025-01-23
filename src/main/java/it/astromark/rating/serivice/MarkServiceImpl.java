package it.astromark.rating.serivice;


import it.astromark.authentication.service.AuthenticationService;
import it.astromark.classmanagement.didactic.repository.TeachingRepository;
import it.astromark.rating.dto.MarkRequest;
import it.astromark.rating.dto.MarkResponse;
import it.astromark.rating.dto.MarkUpdateRequest;
import it.astromark.rating.dto.SemesterReportResponse;
import it.astromark.rating.mapper.MarkMapper;
import it.astromark.rating.model.Mark;
import it.astromark.rating.repository.MarkRepository;
import it.astromark.rating.repository.SemesterReportRepository;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.service.StudentService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;
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
    private final SchoolUserService schoolUserService;
    private final SemesterReportRepository semesterReportRepository;
    private final AuthenticationService authenticationService;
    private final TeachingRepository teachingRepository;
    private final StudentService studentService;

    @Autowired
    public MarkServiceImpl(MarkRepository markRepository, MarkMapper markMapper, SchoolUserService schoolUserService, SemesterReportRepository semesterReportRepository, AuthenticationService authenticationService, TeachingRepository teachingRepository, StudentService studentService) {
        this.markRepository = markRepository;
        this.markMapper = markMapper;
        this.schoolUserService = schoolUserService;
        this.semesterReportRepository = semesterReportRepository;
        this.authenticationService = authenticationService;
        this.teachingRepository = teachingRepository;
        this.studentService = studentService;
    }

    @Override
    @PreAuthorize("hasRole('STUDENT') || hasRole('PARENT')")
    public List<MarkResponse> getMarkByYear(UUID studentId, Year year) {
        if (!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        return markMapper.toMarkResponseList(markRepository.findMarkByStudentIdAndDateBetween(studentId, LocalDate.of(year.getValue(), Month.SEPTEMBER, 1),
                LocalDate.of(year.getValue() + 1, Month.AUGUST, 31)));
    }

    @Override
    @PreAuthorize("hasRole('STUDENT') || hasRole('PARENT')")
    public Double getAverage(UUID studentId, Year year) {
        return getMarkByYear(studentId, year).stream()
                .mapToDouble(MarkResponse::mark)
                .average()
                .orElse(0.0);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT') || hasRole('PARENT') || hasRole('TEACHER')")
    public SemesterReportResponse getReport(@NotNull UUID studentId, @PositiveOrZero Short year, Boolean semester) {
        if (!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if (!schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if (!schoolUserService.isLoggedTeacherStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        var report = semesterReportRepository.findByStudent_IdAndFirstSemesterAndYear(studentId, semester, year);
        if (report.isEmpty()) {
            return null;
        } else if (!report.getFirst().getPublicField() && authenticationService.isStudent()) {
            throw new AccessDeniedException("You are not allowed to access this resource") {
            };
        }


        return markMapper.toSemesterReportResponse(report.getFirst());

    }

    @Override
    @PreAuthorize("hasRole('PARENT')")
    public SemesterReportResponse viewReport(Integer reportId) {
        var report = semesterReportRepository.findById(reportId).orElseThrow();

        if (!schoolUserService.isLoggedUserParent(report.getStudent().getId())) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        report.setViewed(true);
        semesterReportRepository.save(report);

        return markMapper.toSemesterReportResponse(report);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public MarkResponse create(MarkRequest mark) {
        if(mark.mark() < 0 || mark.mark() > 10) {
            throw new IllegalArgumentException("Mark must be between 0 and 10");
        }
        var teacher = authenticationService.getTeacher().orElseThrow();
        var teaching = teachingRepository.findById(mark.teachingId()).orElseThrow();
        if (!teaching.getTeacher().equals(teacher)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        if(!schoolUserService.isLoggedTeacherStudent(mark.studentId())) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        return markMapper.toMarkResponse(markRepository.save(Mark.builder()
                .mark(mark.mark())
                .date(mark.date())
                .student(studentService.getById(mark.studentId()))
                .teaching(teaching)
                .type(mark.type())
                .description(mark.description())
                .build()));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public MarkResponse update(MarkUpdateRequest mark, UUID studentId) {
        if(mark.mark() < 0 || mark.mark() > 10) {
            throw new IllegalArgumentException("Mark must be between 0 and 10");
        }
        if(!schoolUserService.isLoggedTeacherStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        var markEntity = markRepository.findById(mark.id()).orElseThrow();
        var teacher = authenticationService.getTeacher().orElseThrow();
        if(!markEntity.getTeaching().getTeacher().equals(teacher)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        markEntity.setMark(mark.mark());
        markEntity.setDescription(mark.description());
        markEntity.setType(mark.type());

        return markMapper.toMarkResponse(markRepository.save(markEntity));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public boolean delete(Integer id) {
        var mark = markRepository.findById(id).orElseThrow();
        var teacher = authenticationService.getTeacher().orElseThrow();
        if(!mark.getTeaching().getTeacher().equals(teacher)) {
            return false;
        }

        markRepository.delete(mark);
        return true;
    }

}
