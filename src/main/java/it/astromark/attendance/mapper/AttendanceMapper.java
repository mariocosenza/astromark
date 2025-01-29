package it.astromark.attendance.mapper;

import it.astromark.attendance.dto.AttendanceResponse;
import it.astromark.attendance.entity.Delay;
import it.astromark.attendance.repository.AbsenceRepository;
import it.astromark.attendance.repository.DelayRepository;
import it.astromark.attendance.service.JustifiableService;
import it.astromark.user.student.entity.Student;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AttendanceMapper {

    @Mapping(target = "isAbsent", source = "student", qualifiedByName = "isAbsent")
    @Mapping(target = "isDelayed", source = "student", qualifiedByName = "isDelayed")
    @Mapping(target = "delayTime", source = "student", qualifiedByName = "getDelayTime")
    @Mapping(target = "delayNeedJustification", source = "student", qualifiedByName = "getDelayNeedJustification")
    @Mapping(target = "totalAbsence", source = "student", qualifiedByName = "getTotalAbsence")
    @Mapping(target = "totalDelay", source = "student", qualifiedByName = "getTotalDelay")
    AttendanceResponse toAttendanceResponse(Student student, @Context LocalDate date, @Context AbsenceRepository absenceRepository, @Context DelayRepository delayRepository, @Context JustifiableService justifiableService);

    List<AttendanceResponse> toAttendanceResponseList(List<Student> students, @Context LocalDate date, @Context AbsenceRepository absenceRepository, @Context DelayRepository delayRepository, @Context JustifiableService justifiableService);

    @Named("isAbsent")
    default Boolean isAbsent(Student student, @Context LocalDate date, @Context AbsenceRepository absenceRepository) {
        return absenceRepository.findAbsenceByStudentAndDate(student, date) != null;
    }

    @Named("isDelayed")
    default Boolean isDelayed(Student student, @Context LocalDate date, @Context DelayRepository delayRepository) {
        return getDelay(student, date, delayRepository) != null;
    }

    @Named("getDelayTime")
    default Instant getDelayTime(Student student, @Context LocalDate date, @Context DelayRepository delayRepository) {
        var delay = getDelay(student, date, delayRepository);
        return delay == null ? null : delay.getDate();
    }

    @Named("getDelayNeedJustification")
    default boolean getDelayNeedJustification(Student student, @Context LocalDate date, @Context DelayRepository delayRepository) {
        var delay = getDelay(student, date, delayRepository);
        return delay != null && delay.getNeedsJustification();
    }

    @Named("getTotalAbsence")
    default int getTotalAbsence(Student student, @Context LocalDate date, @Context JustifiableService justifiableService) {
        int year = date.getYear() + (date.getMonthValue() < 6 ? -1 : 0);
        return justifiableService.getTotalAbsences(student.getId(), Year.of(year));
    }

    @Named("getTotalDelay")
    default int getTotalDelay(Student student, @Context LocalDate date, @Context JustifiableService justifiableService) {
        int year = date.getYear() + (date.getMonthValue() < 6 ? -1 : 0);
        return justifiableService.getTotalDelays(student.getId(), Year.of(year));
    }

    default Delay getDelay(Student student, LocalDate date, DelayRepository delayRepository) {
        var dateStart = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        var dateEnd = date.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        var delay = delayRepository.findDelayByDateBetweenAndStudent_IdOrderByDateDesc(dateStart, dateEnd, student.getId());
        return delay.isEmpty() ? null : delay.getFirst();
    }
}

