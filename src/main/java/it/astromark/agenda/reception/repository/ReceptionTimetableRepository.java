package it.astromark.agenda.reception.repository;

import it.astromark.agenda.reception.entity.ReceptionTimetable;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceptionTimetableRepository extends JpaRepository<ReceptionTimetable, Integer> {
    List<ReceptionTimetable> findByTeacher(@NotNull Teacher teacher);
}
