package it.astromark.agenda.schoolclass.repository;

import it.astromark.agenda.schoolclass.entity.ClassTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClassTimetableRepository extends JpaRepository<ClassTimetable, Integer> {

    ClassTimetable getClassTimetableBySchoolClass_IdAndEndValidity(Integer schoolClass_id, LocalDate endValidity);

    List<ClassTimetable> getClassTimetableBySchoolClass_Id(Integer schoolClass_id);
}
