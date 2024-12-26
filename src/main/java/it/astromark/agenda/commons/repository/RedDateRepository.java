package it.astromark.agenda.commons.repository;

import it.astromark.agenda.commons.entity.RedDate;
import it.astromark.agenda.commons.entity.RedDateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedDateRepository extends JpaRepository<RedDate, RedDateId> {
}
