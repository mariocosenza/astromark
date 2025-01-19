package it.astromark.attendance.repository;

import it.astromark.attendance.entity.Delay;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface DelayRepository extends JpaRepository<Delay, UUID> {
    int countDelayByDateBetweenAndStudent_Id(@NotNull Instant dateAfter, @NotNull Instant dateBefore, UUID studentId);

    List<Delay> findDelayByDateBetweenAndStudent_IdOrderByDateDesc(@NotNull Instant dateAfter, @NotNull Instant dateBefore, UUID studentId);

}
