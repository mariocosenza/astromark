package it.astromark.user.student.repository;

import it.astromark.school.SchoolRepository;
import it.astromark.school.entity.School;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.student.entity.Student;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Testcontainers
@ActiveProfiles(value = "test")
@Slf4j
class StudentRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.2");

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    private static Validator validator;

    private static School school;

    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator =  factory.getValidator();
        }
        school = School.builder()
                .code("SS23456")
                .name("Liceo Severi")
                .phoneNumber(432435L)
                .address("Viale L. D’Orsi, 5 80053 - Castellammare di Stabia (NA)")
                .email("naps110002@istruzione.it").build();
    }


    @Test
    void save() {
        school = schoolRepository.save(school);
        var faker = new Faker();
        var name = faker.name().firstName();
        var surname = faker.name().lastName();
        var student = studentRepository.save(Student.builder()
                .email(faker.internet().emailAddress())
                .name(name)
                .pendingState(PendingState.FIRST_LOGIN)
                .surname(surname)
                .password(faker.internet().password(8, 16, true, true))
                .residentialAddress(faker.address().fullAddress())
                .gender(true)
                .birthDate(LocalDate.of(2003, 5, 22))
                .username(name + "." + surname)
                .school(school).build());
        assertNotNull(studentRepository.findById(student.getId()));
        assertTrue(validator.validate(student).isEmpty());
    }

}