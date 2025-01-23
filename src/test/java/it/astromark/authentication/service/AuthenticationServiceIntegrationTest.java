package it.astromark.authentication.service;

import com.google.common.hash.Hashing;
import it.astromark.SpringTestConf;
import it.astromark.authentication.dto.UserLoginRequest;
import it.astromark.school.entity.School;
import it.astromark.school.repository.SchoolRepository;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.student.entity.Student;
import it.astromark.user.student.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles(value = "test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import({SpringTestConf.class})
class AuthenticationServiceIntegrationTest {


    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.2");

    private School school;

    private static Student student;

    private static final Faker faker = new Faker();
    @Autowired
    private SchoolRepository schoolRepository;


    @BeforeEach
    public void setUpUser() {
        school = School.builder()
                .code("SS23456")
                .name("Liceo Severi")
                .phoneNumber(432435L)
                .address("Viale L. D’Orsi, 5 80053 - Castellammare di Stabia (NA)")
                .email("naps110002@istruzione.it").build();
        school = schoolRepository.save(school);
    }

    @Test
    void tc1_01() {
        var name = "pluto";
        var surname = "pippo";
        var password = "Pluto123!";
        student = Student.builder()
                .email(faker.internet().emailAddress())
                .name(name)
                .pendingState(PendingState.NORMAL)
                .surname(surname)
                .password(Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString()) //unsafe
                .residentialAddress(faker.address().fullAddress())
                .male(true)
                .birthDate(LocalDate.of(2003, 5, 22))
                .username(name + "." + surname)
                .school(school).build();
       student = studentRepository.save(student);
            var request = new UserLoginRequest(student.getUsername(), password, student.getSchool().getCode(), "STUDENT");
            var result = authenticationService.login(request);
            assertNotNull(result);
    }

    @Test
    void tc1_02() {
        var password = "Pluto12!";
        student.setPassword(Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString());
        student = studentRepository.save(student);
        var request = new UserLoginRequest(student.getUsername(), password, student.getSchool().getCode(), "STUDENT");
            var result = authenticationService.login(request);
            assertNotNull(result);
    }

    @Test
    void tc1_03() {
        var name = "pl";
        var surname = "pi";
        var password = "Pluto123!";
        var student = Student.builder()
                .email(faker.internet().emailAddress())
                .name(name)
                .pendingState(PendingState.NORMAL)
                .surname(surname)
                .password(Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString()) //unsafe
                .residentialAddress(faker.address().fullAddress())
                .male(true)
                .birthDate(LocalDate.of(2003, 5, 22))
                .username(name + "." + surname)
                .school(school).build();
        student = studentRepository.save(student);
        var request = new UserLoginRequest(student.getUsername(), password, student.getSchool().getCode(), "STUDENT");
            var result = authenticationService.login(request);
            assertNotNull(result);
    }

    @Test
    void tc1_04() {
        var name = "pluton";
        var surname = "paperino";
        var password = "Pluto12!";
        var student = Student.builder()
                .email(faker.internet().emailAddress())
                .name(name)
                .pendingState(PendingState.NORMAL)
                .surname(surname)
                .password(Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString()) //unsafe
                .residentialAddress(faker.address().fullAddress())
                .male(true)
                .birthDate(LocalDate.of(2003, 5, 22))
                .username(name + "." + surname)
                .school(school).build();
        student = studentRepository.save(student);
        var request = new UserLoginRequest(student.getUsername(), password, student.getSchool().getCode(), "STUDENT");
            var result = authenticationService.login(request);
            assertNotNull(result);

    }

    @Test
    void tc1_05() {
        var request = new UserLoginRequest("pluto.pippo", "Pluto123!", "SS1234", "STUDENT");
        assertThrows(IllegalArgumentException.class, () -> authenticationService.login(request));
    }

    @Test
    void tc1_06() {

        var request = new UserLoginRequest("SS123456", "Pluto123!", "SS123456", "STUDENT");
        assertThrows(IllegalArgumentException.class, () -> authenticationService.login(request));
    }

    @Test
    void tc1_07() {
        var request = new UserLoginRequest("pluto.pippo", "Pluto123!", "SSSSSSS", "STUDENT");
        assertThrows(IllegalArgumentException.class, () -> authenticationService.login(request));
    }

    @Test
    void tc1_08() {
        var request = new UserLoginRequest("pluto.pippo", "Pluto123!", "SS00000", "STUDENT");
        var result = authenticationService.login(request);
        assertNull(result);
    }

    @Test
    void tc1_09() {
        var request = new UserLoginRequest("p.p", "Pluto123!", "SS12345", "STUDENT");
        assertThrows(IllegalArgumentException.class, () -> authenticationService.login(request));
    }

    @Test
    void tc1_10() {
        var request = new UserLoginRequest("plutopippo.paperino", "Pluto123!", "SS12345", "STUDENT");
        var result = authenticationService.login(request);
        assertNull(result);
    }

    @Test
    void tc1_11() {
        var request = new UserLoginRequest("pippo_pluto", "Pluto123!", "SS12345", "STUDENT");
        var result = authenticationService.login(request);
        assertNull(result);
    }

    @Test
    void tc1_12() {
        var request = new UserLoginRequest("pluto.pippo", "Pluto123!", "SS67890", "STUDENT");
        var result = authenticationService.login(request);
        assertNull(result);
    }

    @Test
    void tc1_13() {
        var name = "pluto";
        var surname = "pippo";
        var password = "Put123!";
        var student = Student.builder()
                .email(faker.internet().emailAddress())
                .name(name)
                .pendingState(PendingState.NORMAL)
                .surname(surname)
                .password(Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString()) //unsafe
                .residentialAddress(faker.address().fullAddress())
                .male(true)
                .birthDate(LocalDate.of(2003, 5, 22))
                .username(name + "." + surname)
                .school(school).build();

        var request = new UserLoginRequest(student.getUsername(), password, "SS12345", "STUDENT");
        assertThrows(IllegalArgumentException.class, () -> authenticationService.login(request));
    }

    @Test
    void tc1_14() {
        var request = new UserLoginRequest("pluto.pippo", "Put1234567!", "SS12345", "STUDENT");
        var result = authenticationService.login(request);
        assertNull(result);
    }

    @Test
    void tc1_15() {
        var request = new UserLoginRequest("pluto.pippo", "Pluto123!", "SS12345", "STUDENT");
        var result = authenticationService.login(request);
        assertNull(result);
    }

}