package it.astromark.classmanagement.didactic.repository;

import it.astromark.classmanagement.didactic.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {



}
