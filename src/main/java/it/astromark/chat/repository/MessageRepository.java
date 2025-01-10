package it.astromark.chat.repository;

import it.astromark.chat.entity.Message;
import it.astromark.chat.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByTicket(Ticket ticket);
}
