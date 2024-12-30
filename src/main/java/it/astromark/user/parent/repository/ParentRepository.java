package it.astromark.user.parent.repository;

import it.astromark.school.entity.School;
import it.astromark.user.parent.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParentRepository extends JpaRepository<Parent, UUID> {

    Parent findByUsernameAndSchoolCode(String username , School schoolCode);
}
