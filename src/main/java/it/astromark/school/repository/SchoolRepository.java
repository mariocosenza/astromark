package it.astromark.school.repository;

import it.astromark.school.entity.School;
import it.astromark.user.secretary.entity.Secretary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SchoolRepository extends JpaRepository<School, String> {
    School findByCode(String code);

    School findBySecretariesContains(Set<Secretary> secretaries);

}
