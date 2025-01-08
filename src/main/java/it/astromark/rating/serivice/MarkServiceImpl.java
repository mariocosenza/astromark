package it.astromark.rating.serivice;


import it.astromark.rating.dto.MarkResponse;
import it.astromark.rating.mapper.MarkMapper;
import it.astromark.rating.model.Mark;
import it.astromark.rating.repository.MarkRepository;
import it.astromark.user.commons.service.SchoolUserService;
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

    @Autowired
    public MarkServiceImpl(MarkRepository markRepository, MarkMapper markMapper, SchoolUserService schoolUserService) {
        this.markRepository = markRepository;
        this.markMapper = markMapper;
        this.schoolUserService = schoolUserService;
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
    public MarkResponse create(Mark mark) {
        return null;
    }

    @Override
    public MarkResponse update(Integer integer, Mark mark) {
        return null;
    }

    @Override
    public MarkResponse delete(Integer integer) {
        return null;
    }

    @Override
    public MarkResponse getById(Integer integer) {
        return null;
    }
}
