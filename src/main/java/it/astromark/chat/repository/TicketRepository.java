package it.astromark.chat.repository;

import it.astromark.chat.entity.Ticket;
import it.astromark.user.parent.entity.Parent;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    List<Ticket> findByTeacherAndClosedAndSolved(Teacher teacher, @NotNull Boolean closed, @NotNull Boolean solved);

    List<Ticket> findByParentAndClosedAndSolved(Parent parent, @NotNull Boolean closed, @NotNull Boolean solved);
}
