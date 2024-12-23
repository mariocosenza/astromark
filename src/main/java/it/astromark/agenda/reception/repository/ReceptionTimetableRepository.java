package it.astromark.agenda.reception.repository;

import it.astromark.agenda.reception.entity.ReceptionTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceptionTimetableRepository extends JpaRepository<ReceptionTimetable, Integer> {
}
