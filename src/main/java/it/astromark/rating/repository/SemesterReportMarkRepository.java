package it.astromark.rating.repository;

import it.astromark.rating.model.SemesterReportMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterReportMarkRepository extends JpaRepository<SemesterReportMark, Integer> {
}
