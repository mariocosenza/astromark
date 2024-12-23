package it.astromark.classwork.repository;

import it.astromark.classwork.entity.ClassActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassActivityRepository extends JpaRepository<ClassActivity, Integer> {
}
