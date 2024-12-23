package it.astromark.agenda.schoolclass.repository;

import it.astromark.agenda.schoolclass.entity.SignedHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignedHourRepository extends JpaRepository<SignedHour, Integer> {
}
