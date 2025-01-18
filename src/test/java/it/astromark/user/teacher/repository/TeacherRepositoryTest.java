package it.astromark.user.teacher.repository;

import com.google.common.hash.Hashing;
import it.astromark.SpringTestConf;
import it.astromark.commons.configuration.SpringValidationConf;
import it.astromark.school.repository.SchoolRepository;
import it.astromark.school.entity.School;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@ActiveProfiles(value = "test")
@Slf4j
@Import({SpringTestConf.class, SpringValidationConf.class})
class TeacherRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.2");

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private Faker faker;

    private static School school;


    @BeforeAll
    public static void setUp() {
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
        var name = faker.name().firstName();
        var surname = faker.name().lastName();
        var teacher = teacherRepository.save(Teacher.builder()
                .email(faker.internet().emailAddress())
                .name(name)
                .pendingState(PendingState.FIRST_LOGIN)
                .surname(surname)
                .password(Hashing.sha512().hashString(faker.internet().password(8, 16, true, true), StandardCharsets.UTF_8).toString())
                .residentialAddress(faker.address().fullAddress())
                .male(true)
                .birthDate(LocalDate.of(2003, 5, 22))
                .username(name + "." + surname)
                .school(school).build());
        assertNotNull(teacherRepository.findById(teacher.getId()));
        assertTrue(validator.validate(teacher).isEmpty());
    }

}