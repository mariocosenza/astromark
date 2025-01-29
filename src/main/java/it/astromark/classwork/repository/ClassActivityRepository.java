package it.astromark.classwork.repository;

import it.astromark.agenda.schoolclass.entity.SignedHour;
import it.astromark.classwork.entity.ClassActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassActivityRepository extends JpaRepository<ClassActivity, Integer> {
    List<ClassActivity> findAllBySignedHour_TeachingTimeslot_ClassTimetable_SchoolClass_Id(Integer signedHour_teachingTimeslot_classTimetable_schoolClass_id);

    ClassActivity findBySignedHour(SignedHour signedHour);
}
