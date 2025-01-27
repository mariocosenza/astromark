package it.astromark.user.commons.controller;

import com.google.common.hash.Hashing;
import it.astromark.school.entity.School;
import it.astromark.school.repository.SchoolRepository;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.student.entity.Student;
import it.astromark.user.student.repository.StudentRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@ExtendWith(SpringExtension.class)
class SchoolUserControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.2");
    private static int count;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private MockMvc mockMvc;
    private String token;
    @Autowired
    private SchoolRepository schoolRepository;


    @BeforeEach
    public void setUpUser() throws Exception {
        School school = School.builder()
                .code("SS23456")
                .name("Liceo Severi")
                .phoneNumber(432435L)
                .address("Viale L. D’Orsi, 5 80053 - Castellammare di Stabia (NA)")
                .email("naps110002@istruzione.it").build();
        school = schoolRepository.save(school);
        var faker = new Faker();
        String password = faker.internet().password(8, 16, true, true);
        var name = "mario";
        var surname = "rossi";
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
        Student student = studentRepository.save(stu);
        var result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + student.getUsername() + "\",\"password\":\"" + password + "\",\"schoolCode\":\"" + school.getCode() + "\",\"role\":\"STUDENT\"}"))
                .andReturn();
        token = result.getResponse().getContentAsString();
    }


    @Test
    void tc2_01() throws Exception {
        var newAddress = "Via Roma 123";

        mockMvc.perform(patch("/api/school-users/address")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.TEXT_HTML)
                        .content(newAddress))
                .andExpect(status().isOk());
    }

    @Test
    void tc2_02() throws Exception {
        var newAddress = "Via X";

        mockMvc.perform(patch("/api/school-users/address")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.TEXT_HTML)
                        .content(newAddress))
                .andExpect(status().isOk());
    }

    @Test
    void tc2_03() throws Exception {
        var newAddress = "Via S. Marco";

        mockMvc.perform(patch("/api/school-users/address")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.TEXT_HTML)
                        .content(newAddress))
                .andExpect(status().isOk());
    }

    @Test
    void tc2_04() throws Exception {
        var newAddress = "Via";

        mockMvc.perform(patch("/api/school-users/address")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.TEXT_HTML)
                        .content(newAddress))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void tc2_05() throws Exception {
        var newAddress = "Via Napoli#";

        mockMvc.perform(patch("/api/school-users/address")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.TEXT_HTML)
                        .content(newAddress))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void tc3_01() throws Exception {
        mockMvc.perform(patch("/api/school-users/preferences")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"Pluto123!\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void tc3_02() throws Exception {
        mockMvc.perform(patch("/api/school-users/preferences")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"Pluto12!\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void tc3_03() throws Exception {
        mockMvc.perform(patch("/api/school-users/preferences")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"Pluto1!\"}"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void tc3_04() throws Exception {
        mockMvc.perform(patch("/api/school-users/preferences")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"Pluto1234\"}"))
                .andExpect(status().is5xxServerError());
    }


}