package it.astromark.agenda.reception.repository;

import it.astromark.agenda.reception.entity.ReceptionTimeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceptionTimeslotRepository extends JpaRepository<ReceptionTimeslot, Integer> {
}
