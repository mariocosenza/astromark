package it.astromark.classwork.repository;

import it.astromark.agenda.schoolclass.entity.SignedHour;
import it.astromark.classwork.entity.ClassActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassActivityRepository extends JpaRepository<ClassActivity, Integer> {
    List<ClassActivity> findBySignedHourTeachingTimeslotClassTimetableSchoolClass_Id(Integer attr0);

    ClassActivity findBySignedHour(SignedHour signedHour);
}
