package it.astromark.agenda.reception.controller;

import com.google.common.hash.Hashing;
import it.astromark.agenda.reception.entity.ReceptionTimeslot;
import it.astromark.agenda.reception.entity.ReceptionTimetable;
import it.astromark.agenda.reception.repository.ReceptionTimeslotRepository;
import it.astromark.agenda.reception.repository.ReceptionTimetableRepository;
import it.astromark.classmanagement.entity.SchoolClass;
import it.astromark.classmanagement.entity.TeacherClass;
import it.astromark.classmanagement.entity.TeacherClassId;
import it.astromark.classmanagement.repository.SchoolClassRepository;
import it.astromark.classmanagement.repository.TeacherClassRepository;
import it.astromark.school.entity.School;
import it.astromark.school.repository.SchoolRepository;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.parent.entity.Parent;
import it.astromark.user.parent.repository.ParentRepository;
import it.astromark.user.student.entity.Student;
import it.astromark.user.student.repository.StudentRepository;
import it.astromark.user.teacher.entity.Teacher;
import it.astromark.user.teacher.repository.TeacherRepository;
import jakarta.transaction.Transactional;
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
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@ExtendWith(SpringExtension.class)
class ReceptionAgendaControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.2");
    private static int count;
    @Autowired
    private ReceptionTimeslotRepository receptionTimeslotRepository;
    private Parent parent;
    private School sc;
    private Teacher teacher;
    private ReceptionTimetable table;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ReceptionTimetableRepository receptionTimetableRepository;
    @Autowired
    private MockMvc mockMvc;
    private String token;
    @Autowired
    private TeacherClassRepository teacherClassRepository;
    @Autowired
    private SchoolClassRepository schoolClassRepository;

    @BeforeEach
    @Transactional
    void setUp() throws Exception {
        var faker = new Faker();
        var school = School.builder()
                .code("SS23456")
                .name("Liceo Severi")
                .phoneNumber(432435L)
                .address("Viale L. D’Orsi, 5 80053 - Castellammare di Stabia (NA)")
                .email("naps110002@istruzione.it").build();
        var name = "pluto" + (char) ('a' + count);
        var surname = "pippo";
        var password = "Pluto123!";
        var schoolClass = SchoolClass.builder()
                .letter("A")
                .school(school)
                .year(2024)
                .number((short) 1)
                .build();
        schoolRepository.save(school);
        schoolClassRepository.save(schoolClass);
        var student = Student.builder()
                .email(faker.internet().emailAddress())
                .name(name)
                .pendingState(PendingState.NORMAL)
                .surname(surname)
                .password(Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString())
                .residentialAddress(faker.address().fullAddress())
                .male(true)
                .birthDate(LocalDate.of(2003, 5, 22))
                .username(name + "." + surname)
                .schoolClasses(Collections.singleton(schoolClass))
                .school(school).build();
        schoolClass.setStudents(Collections.singleton(student));
        studentRepository.save(student);
        parent = Parent.builder()
                .email(faker.internet().emailAddress())
                .name(name)
                .pendingState(PendingState.NORMAL)
                .surname(surname)
                .school(school)
                .legalGuardian(false)
                .male(true)
                .username(name + "." + surname)
                .birthDate(LocalDate.of(1990, 5, 22))
                .password(Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString()) //unsafe
                .residentialAddress(faker.address().fullAddress())
                .student(student)
                .build();
        parentRepository.save(parent);
        teacher = Teacher.builder()
                .email(faker.internet().emailAddress())
                .name(name)
                .taxId("ABCDEF12G34H567" + ++count)
                .residentialAddress(faker.address().fullAddress())
                .pendingState(PendingState.NORMAL)
                .surname(surname)
                .male(true)
                .username(name + "." + surname)
                .birthDate(LocalDate.of(1990, 5, 22))
                .pendingState(PendingState.NORMAL)
                .school(school)
                .password(Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString())
                .build();
        teacherRepository.save(teacher);
        var teacherClass = new TeacherClass(new TeacherClassId(teacher.getId(), schoolClass.getId()), teacher, schoolClass, false);
        teacherClassRepository.save(teacherClass);
        teacher.setTeacherClasses(Set.of(teacherClass));
        teacherRepository.save(teacher);

        table = ReceptionTimetable.builder()
                .textInfoReception("info")
                .startValidity(LocalDate.now().minusDays(200))
                .teacher(teacher)
                .build();
        receptionTimetableRepository.save(table);
        var result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + parent.getUsername() + "\",\"password\":\"" + password + "\",\"schoolCode\":\"" + school.getCode() + "\",\"role\":\"PARENT\"}"))
                .andReturn();
        token = result.getResponse().getContentAsString();
    }

    @Test
    void tc4_01() throws Exception {
        var slot = ReceptionTimeslot
                .builder().date(LocalDate.now().plusDays(1)).mode("In Presenza").hour((short) 1).receptionTimetable(table).booked((short) 4).capacity((short) 6).build();
        receptionTimeslotRepository.save(slot);
        assertTrue(mockMvc.perform(patch("/api/agenda/reception/timeslot/" + slot.getId() + "/book")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.TEXT_HTML))
                .andReturn().getResponse().getContentAsString().contains("true"));
    }

    @Test
    void tc4_02() throws Exception {
        var slot = ReceptionTimeslot
                .builder().date(LocalDate.now().plusDays(1)).mode("Online").hour((short) 2).receptionTimetable(table).booked((short) 4).capacity((short) 6).build();

        receptionTimeslotRepository.save(slot);
        assertTrue(mockMvc.perform(patch("/api/agenda/reception/timeslot/" + slot.getId() + "/book")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.TEXT_HTML))
                .andReturn().getResponse().getContentAsString().contains("true"));

    }

    @Test
    void tc4_03() throws Exception {
        var slot = ReceptionTimeslot
                .builder().date(LocalDate.now().plusDays(1)).mode("In Presenza").hour((short) 3).receptionTimetable(table).booked((short) 0).capacity((short) 6).build();
        receptionTimeslotRepository.save(slot);
        assertTrue(mockMvc.perform(patch("/api/agenda/reception/timeslot/" + slot.getId() + "/book")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.TEXT_HTML))
                .andReturn().getResponse().getContentAsString().contains("true"));

    }

    @Test
    void tc4_04() throws Exception {
        var slot = ReceptionTimeslot
                .builder().date(LocalDate.now().plusDays(1)).mode("In Presenza").hour((short) 4).receptionTimetable(table).booked((short) 0).capacity((short) 1).build();
        receptionTimeslotRepository.save(slot);
        assertTrue(mockMvc.perform(patch("/api/agenda/reception/timeslot/" + slot.getId() + "/book")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.TEXT_HTML))
                .andReturn().getResponse().getContentAsString().contains("true"));

    }

    @Test
    void tc4_05() throws Exception {
        var slot = ReceptionTimeslot
                .builder().date(LocalDate.now().plusDays(1)).mode("In Presenza").hour((short) 5).receptionTimetable(table).booked((short) 6).capacity((short) 6).build();
        receptionTimeslotRepository.save(slot);
        assertFalse(mockMvc.perform(patch("/api/agenda/reception/timeslot/" + slot.getId() + "/book")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.TEXT_HTML))
                .andReturn().getResponse().getContentAsString().contains("true"));

    }

    @Test
    void tc4_06() throws Exception {
        var slot = ReceptionTimeslot
                .builder().date(LocalDate.now().plusDays(1)).mode("In Presenza").hour((short) 6).receptionTimetable(table).booked((short) 1).capacity((short) 1).build();
        receptionTimeslotRepository.save(slot);
        assertFalse(mockMvc.perform(patch("/api/agenda/reception/timeslot/" + slot.getId() + "/book")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.TEXT_HTML))
                .andReturn().getResponse().getContentAsString().contains("true"));
    }
}