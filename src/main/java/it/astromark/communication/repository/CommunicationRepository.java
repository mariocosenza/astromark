package it.astromark.communication.repository;


import it.astromark.communication.entity.Communication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunicationRepository extends JpaRepository<Communication, Integer> {
    List<Communication> findBySchoolClass_Id(Integer schoolClassId);
}
