package it.astromark.agenda.reception.repository;

import it.astromark.agenda.reception.entity.ReceptionBooking;
import it.astromark.agenda.reception.entity.ReceptionBookingId;
import it.astromark.user.parent.entity.Parent;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceptionBookingRepository extends JpaRepository<ReceptionBooking, ReceptionBookingId> {
    List<ReceptionBooking> findByReceptionTimeslot_ReceptionTimetable_Teacher(@NotNull Teacher receptionTimeslotReceptionTimetableTeacher);

    List<ReceptionBooking> findByParent(@NotNull Parent parent);
}
