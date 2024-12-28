package it.astromark.school;

import it.astromark.school.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, String> {

    School findByCode(String code);
}
