package it.astromark.agenda.schoolclass.repository;

import it.astromark.agenda.schoolclass.entity.ClassTimetable;
import it.astromark.agenda.schoolclass.entity.TeachingTimeslot;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TeachingTimeslotRepository extends JpaRepository<TeachingTimeslot, Integer> {
    List<TeachingTimeslot> findTeachingTimeslotByClassTimetableSchoolClass_IdAndDateBetween(Integer classTimetableId, @NotNull LocalDate dateAfter, @NotNull LocalDate dateBefore);

    List<TeachingTimeslot> findTeachingTimeslotByClassTimetableAndDate(@NotNull ClassTimetable classTimetable, @NotNull LocalDate date);
}
