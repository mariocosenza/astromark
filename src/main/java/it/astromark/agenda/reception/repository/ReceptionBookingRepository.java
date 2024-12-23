package it.astromark.agenda.reception.repository;

import it.astromark.agenda.reception.entity.ReceptionBooking;
import it.astromark.agenda.reception.entity.ReceptionBookingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceptionBookingRepository extends JpaRepository<ReceptionBooking, ReceptionBookingId> {
}
