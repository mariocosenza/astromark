package it.astromark.attendance.service;

import it.astromark.attendance.dto.JustifiableResponse;
import it.astromark.attendance.mapper.JustifiableMapper;
import it.astromark.attendance.repository.AbsenceRepository;
import it.astromark.attendance.repository.DelayRepository;
import it.astromark.user.commons.service.SchoolUserService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
public class JustifiableServiceImpl implements JustifiableService {

    private final SchoolUserService schoolUserService;
    private final AbsenceRepository absenceRepository;
    private final DelayRepository delayRepository;
    private final JustifiableMapper justifiableMapper;



    @Autowired
    public JustifiableServiceImpl(SchoolUserService schoolUserService, AbsenceRepository absenceRepository, DelayRepository delayRepository, JustifiableMapper justifiableMapper) {
        this.schoolUserService = schoolUserService;
        this.absenceRepository = absenceRepository;
        this.delayRepository = delayRepository;
        this.justifiableMapper = justifiableMapper;
    }

    @Override
    @PreAuthorize("hasRole('student')") //temp student
    public JustifiableResponse justify(@NotNull UUID studentId, @NotNull UUID justificationId, @NotEmpty @Size(max = 512) String justificationText, @NotNull Boolean absence) {
        if(!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        if(absence) {
            var justifiableEntity = absenceRepository.findById(justificationId).orElseThrow(() -> new DataAccessException("Absence not found") {
            });
            if(justifiableEntity.getJustified() || !justifiableEntity.getNeedsJustification()) {
                throw new DataAccessException("Absence already justified") {
                };
            }
            justifiableEntity.setJustificationText(justificationText);
            justifiableEntity.setJustified(true);
            absenceRepository.save(justifiableEntity);
            return justifiableMapper.toJustifiableResponse(justifiableEntity);
        } else {
            var justifiableEntity = delayRepository.findById(justificationId).orElseThrow(() -> new DataAccessException("Absence not found") {
            });
            if (justifiableEntity.getJustified() || !justifiableEntity.getNeedsJustification()) {
                throw new DataAccessException("Delay already justified") {
                };
            }
            justifiableEntity.setJustified(true);
            justifiableEntity.setJustificationText(justificationText);
            delayRepository.save(justifiableEntity);
            return justifiableMapper.toJustifiableResponse(justifiableEntity);
        }
    }

    @Override
    @PreAuthorize("hasRole('parent') || hasRole('student')")
    public List<JustifiableResponse> getAbsencesByYear(UUID studentId, Year year) {
        if(!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if(!schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        return justifiableMapper.absenceToJustifiableResponseList(absenceRepository.findAbsenceOByDateBetweenAndStudent_IdOrderByDateDesc(LocalDate.of(year.getValue(), Month.SEPTEMBER, 1),
                LocalDate.of(year.getValue() + 1, Month.AUGUST, 31), studentId));
    }

    @Override
    @PreAuthorize("hasRole('parent') || hasRole('student')")
    public List<JustifiableResponse> getDelayByYear(UUID studentId, Year year) {
        if(!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if(!schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        return justifiableMapper.delayToJustifiableResponseList(delayRepository.findDelayByDateBetweenAndStudent_IdOrderByDateDesc(LocalDate.of(year.getValue(), Month.SEPTEMBER, 1).atStartOfDay().toInstant(ZoneOffset.UTC),
                LocalDate.of(year.getValue() + 1, Month.AUGUST, 31).atStartOfDay().toInstant(ZoneOffset.UTC), studentId));
    }

    @Override
    @PreAuthorize("hasRole('parent') || hasRole('student')")
    public Integer getTotalAbsences(UUID studentId, Year year) {
        if(!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if(!schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        return absenceRepository.countAbsenceByDateBetweenAndStudent_Id(LocalDate.of(year.getValue(), Month.SEPTEMBER, 1), LocalDate.of(year.getValue() + 1, Month.AUGUST, 31), studentId);
    }

    @Override
    @PreAuthorize("hasRole('parent') || hasRole('student')")
    public Integer getTotalDelays(UUID studentId, Year year) {
        if(!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if(!schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        return delayRepository.countDelayByDateBetweenAndStudent_Id(LocalDate.of(year.getValue(), Month.SEPTEMBER, 1).atStartOfDay().toInstant(ZoneOffset.UTC), LocalDate.of(year.getValue() + 1, Month.AUGUST, 31).atStartOfDay().toInstant(ZoneOffset.UTC), studentId);
    }
}
