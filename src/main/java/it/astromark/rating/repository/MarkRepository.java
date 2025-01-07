package it.astromark.rating.repository;

import it.astromark.rating.model.Mark;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Integer> {
    List<Mark> findMarkByStudentIdAndDateBetween(UUID uuid, @NotNull LocalDate before, @NotNull LocalDate after);

}
