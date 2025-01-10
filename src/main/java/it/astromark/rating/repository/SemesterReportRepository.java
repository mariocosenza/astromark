package it.astromark.rating.repository;

import it.astromark.rating.model.SemesterReport;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SemesterReportRepository extends JpaRepository<SemesterReport, Integer> {
    List<SemesterReport> findByStudent_IdAndFirstSemesterAndYear(UUID studentId, @NotNull Boolean firstSemester, @PositiveOrZero Short year);
}
