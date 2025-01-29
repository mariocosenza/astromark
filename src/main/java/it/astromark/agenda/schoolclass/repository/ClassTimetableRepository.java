package it.astromark.agenda.schoolclass.repository;

import it.astromark.agenda.schoolclass.entity.ClassTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClassTimetableRepository extends JpaRepository<ClassTimetable, Integer> {

    List<ClassTimetable> getClassTimetableBySchoolClass_Id(Integer schoolClass_id);

    Optional<ClassTimetable> getClassTimetableBySchoolClass_IdAndEndValidityAfter(Integer classId, LocalDate now);

    Optional<ClassTimetable> getClassTimetableBySchoolClass_IdAndEndValidityIsNull(Integer schoolClassId);
}
