package it.astromark.agenda.schoolclass.repository;

import it.astromark.agenda.schoolclass.entity.ClassTimetable;
import it.astromark.agenda.schoolclass.entity.TeachingTimeslot;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeachingTimeslotRepository extends JpaRepository<TeachingTimeslot, Integer> {
    List<TeachingTimeslot> findByClassTimetable_SchoolClass_IdAndDateBetween(Integer classTimetableSchoolClassId, @NotNull LocalDate dateAfter, @NotNull LocalDate dateBefore);

    List<TeachingTimeslot> findTeachingTimeslotByClassTimetableAndDate(@NotNull ClassTimetable classTimetable, @NotNull LocalDate date);

    List<TeachingTimeslot> findByClassTimetableId(@NotNull Integer classTimetableId);

    Optional<TeachingTimeslot> findByClassTimetableAndDateAndHour(ClassTimetable timeTable, LocalDate finalStartDate, Short hour);
}
