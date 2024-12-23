package it.astromark.agenda.schoolclass.repository;

import it.astromark.agenda.schoolclass.entity.TeachingTimeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachingTimeslotRepository extends JpaRepository<TeachingTimeslot, Integer> {
}
