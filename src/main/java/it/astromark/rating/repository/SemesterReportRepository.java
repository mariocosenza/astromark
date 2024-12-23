package it.astromark.rating.repository;

import it.astromark.rating.model.SemesterReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterReportRepository extends JpaRepository<SemesterReport, Integer> {
}
