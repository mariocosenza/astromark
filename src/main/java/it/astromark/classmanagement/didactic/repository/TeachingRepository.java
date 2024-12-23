package it.astromark.classmanagement.didactic.repository;

import it.astromark.classmanagement.didactic.entity.Teaching;
import it.astromark.classmanagement.didactic.entity.TeachingId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeachingRepository extends JpaRepository<Teaching, TeachingId> {
}
