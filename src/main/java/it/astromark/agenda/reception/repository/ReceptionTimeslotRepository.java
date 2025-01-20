package it.astromark.agenda.reception.repository;

import it.astromark.agenda.reception.entity.ReceptionTimeslot;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ReceptionTimeslotRepository extends JpaRepository<ReceptionTimeslot, Integer> {
    Page<ReceptionTimeslot> findAllByReceptionTimetable_TeacherAndDateAfter(@NotNull Teacher receptionTimetableTeacher, @NotNull LocalDate dateAfter, Pageable pageable);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    ReceptionTimeslot findByIdAndDateAfter(Integer id, @NotNull LocalDate dateAfter);

}
