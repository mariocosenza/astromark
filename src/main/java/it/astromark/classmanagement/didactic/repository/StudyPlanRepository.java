package it.astromark.classmanagement.didactic.repository;

import it.astromark.classmanagement.didactic.entity.StudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyPlanRepository extends JpaRepository<StudyPlan, Integer> {
}
