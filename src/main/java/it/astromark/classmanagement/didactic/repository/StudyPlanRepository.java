package it.astromark.classmanagement.didactic.repository;

import it.astromark.classmanagement.didactic.entity.StudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyPlanRepository extends JpaRepository<StudyPlan, Integer> {
    List<StudyPlan> findBySchoolClass_Id(Integer schoolClass_id);
}
