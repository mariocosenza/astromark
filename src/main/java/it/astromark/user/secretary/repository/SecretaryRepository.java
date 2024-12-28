package it.astromark.user.secretary.repository;

import it.astromark.school.entity.School;
import it.astromark.user.secretary.entity.Secretary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SecretaryRepository extends JpaRepository<Secretary, UUID> {

    Secretary findByUsernameAndSchoolCode(String username , School schoolCode);
}
