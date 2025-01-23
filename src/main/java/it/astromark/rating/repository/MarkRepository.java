package it.astromark.rating.repository;

import it.astromark.classmanagement.didactic.entity.Teaching;
import it.astromark.rating.model.Mark;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Integer> {
    List<Mark> findMarkByStudentIdAndDateBetween(UUID uuid, @NotNull LocalDate before, @NotNull LocalDate after);

    @Query("SELECT m FROM Mark m " +
            "JOIN m.student s " +
            "JOIN s.schoolClasses sc " +
            "WHERE sc.id = :schoolClassId AND m.date = :date AND m.teaching = :teaching")
    List<Mark> findAllMarksBySchoolClassAndDateAndTeaching(@Param("schoolClassId") Integer schoolClassId, @Param("date") LocalDate date, @Param("teaching") Teaching teaching);
}
