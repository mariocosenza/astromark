package it.astromark.classwork.repository;

import it.astromark.agenda.schoolclass.entity.SignedHour;
import it.astromark.classwork.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Integer> {
    Homework findBySignedHour(SignedHour signedHour);
}
