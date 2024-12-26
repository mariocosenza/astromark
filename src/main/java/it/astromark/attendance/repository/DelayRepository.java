package it.astromark.attendance.repository;

import it.astromark.attendance.entity.Delay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DelayRepository extends JpaRepository<Delay, UUID> {
}
