package it.astromark.user.commons.service;

import com.google.common.hash.Hashing;
import it.astromark.SpringTestConf;
import it.astromark.authentication.dto.UserLoginRequest;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.school.entity.School;
import it.astromark.school.repository.SchoolRepository;
import it.astromark.user.commons.dto.SchoolUserUpdate;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@ActiveProfiles(value = "test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import({SpringTestConf.class})
class SchoolUserServiceIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.2");
    private static int count;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SchoolUserServiceImpl schoolUserService;
    @Autowired
    private SchoolRepository schoolRepository;
    private Student student;


    @BeforeEach
    public void setUpUser() {
        School school = School.builder()
                .code("SS23456")
                .name("Liceo Severi")
                .phoneNumber(432435L)
                .address("Viale L. D’Orsi, 5 80053 - Castellammare di Stabia (NA)")
                .email("naps110002@istruzione.it").build();
        school = schoolRepository.save(school);
        var faker = new Faker();
        var name = "mario";
        var surname = "rossi";
        var password = "Pluto123!";
        var stu = Student.builder()
                .email(faker.internet().emailAddress())
                .name(name)
                .pendingState(PendingState.NORMAL)
                .surname(surname)
                .password(Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString())
                .residentialAddress(faker.address().fullAddress())
                .male(true)
                .birthDate(LocalDate.of(2003, 5, 22))
                .username(name + "." + surname + count++)
                .school(school).build();
        student = studentRepository.save(stu);
        authenticationService.login(new UserLoginRequest(student.getUsername(), password, student.getSchool().getCode(), "STUDENT"));
    }

    @Test
    void tc2_01() {
        var newAddress = "Via Roma 123";
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(student, null, List.of(authenticationService.getRole(student)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertDoesNotThrow(() -> schoolUserService.updateAddress(newAddress));

    }

    @Test
    void tc2_02() {
        var newAddress = "Via X";
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(student, null, List.of(authenticationService.getRole(student)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertDoesNotThrow(() -> schoolUserService.updateAddress(newAddress)); // Verify address update

    }

    @Test
    void tc2_03() {
        var newAddress = "Via S. Marco";
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(student, null, List.of(authenticationService.getRole(student)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertDoesNotThrow(() -> schoolUserService.updateAddress(newAddress)); // Verify address update
    }

    @Test
    void tc2_04() {
        var newAddress = "Via";
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(student, null, List.of(authenticationService.getRole(student)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertThrows(IllegalArgumentException.class, () -> schoolUserService.updateAddress(newAddress)); // Verify address update
    }

    @Test
    void tc2_05() {
        var newAddress = "Via Napoli#";
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(student, null, List.of(authenticationService.getRole(student)));
        SecurityContextHolder.getContext().setAuthentication(token);

        assertThrows(IllegalArgumentException.class, () -> schoolUserService.updateAddress(newAddress)); // Verify address update
    }

    @Test
    void tc3_01() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(student, null, List.of(authenticationService.getRole(student)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertDoesNotThrow(() -> schoolUserService.updatePreferences(new SchoolUserUpdate("Pluto123!"))); // Verify password update
    }

    @Test
    void tc3_02() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(student, null, List.of(authenticationService.getRole(student)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertDoesNotThrow(() -> schoolUserService.updatePreferences(new SchoolUserUpdate("Pluto12!"))); // Verify password update
    }

    @Test
    void tc3_03() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(student, null, List.of(authenticationService.getRole(student)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertThrows(IllegalArgumentException.class, () -> schoolUserService.updatePreferences(new SchoolUserUpdate("Pluto1!"))); // Verify password update
    }

    @Test
    void tc3_04() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(student, null, List.of(authenticationService.getRole(student)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertThrows(IllegalArgumentException.class, () -> schoolUserService.updatePreferences(new SchoolUserUpdate("Pluto1234"))); // Verify password update
    }

    @Test
    void tc3_05() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(student, null, List.of(authenticationService.getRole(student)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertThrows(IllegalArgumentException.class, () -> schoolUserService.updatePreferences(new SchoolUserUpdate("Pluto1234"))); // Verify password update
    }

}