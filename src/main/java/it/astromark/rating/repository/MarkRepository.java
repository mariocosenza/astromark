package it.astromark.rating.repository;

import it.astromark.rating.model.Mark;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Integer> {
    List<Mark> findMarkByDateBetween(@NotNull LocalDate dateAfter, @NotNull LocalDate dateBefore);

}
