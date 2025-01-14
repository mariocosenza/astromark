package it.astromark.chat.repository;

import it.astromark.chat.entity.HomeworkChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HomeworkChatRepository extends JpaRepository<HomeworkChat, UUID> {

}
