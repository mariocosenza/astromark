package it.astromark.agenda.schoolclass.repository;

import it.astromark.agenda.schoolclass.entity.ClassTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ClassTimetableRepository extends JpaRepository<ClassTimetable, Integer> {

    ClassTimetable getClassTimetableBySchoolClass_IdAndEndValidity(Integer schoolClass_id, LocalDate endValidity);
}
