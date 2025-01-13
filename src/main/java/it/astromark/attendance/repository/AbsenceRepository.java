package it.astromark.attendance.repository;

import it.astromark.attendance.entity.Absence;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, UUID> {
    int countAbsenceByDateBetweenAndStudent_Id(@NotNull LocalDate dateAfter, @NotNull LocalDate dateBefore, UUID studentId);
    List<Absence> findAbsenceOByDateBetweenAndStudent_IdOrderByDateDesc(@NotNull LocalDate dateAfter, @NotNull LocalDate dateBefore, UUID studentId);
}
