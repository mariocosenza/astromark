package it.astromark.rating.serivice;

import com.google.common.hash.Hashing;
import it.astromark.SpringTestConf;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.classmanagement.didactic.entity.Subject;
import it.astromark.classmanagement.didactic.entity.Teaching;
import it.astromark.classmanagement.didactic.entity.TeachingId;
import it.astromark.classmanagement.didactic.repository.SubjectRepository;
import it.astromark.classmanagement.didactic.repository.TeachingRepository;
import it.astromark.classmanagement.entity.SchoolClass;
import it.astromark.classmanagement.entity.TeacherClass;
import it.astromark.classmanagement.entity.TeacherClassId;
import it.astromark.classmanagement.repository.SchoolClassRepository;
import it.astromark.classmanagement.repository.TeacherClassRepository;
import it.astromark.rating.dto.MarkRequest;
import it.astromark.rating.model.MarkType;
import it.astromark.rating.service.MarkServiceImpl;
import it.astromark.school.entity.School;
import it.astromark.school.repository.SchoolRepository;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.student.entity.Student;
import it.astromark.user.student.repository.StudentRepository;
import it.astromark.user.teacher.entity.Teacher;
import it.astromark.user.teacher.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@ActiveProfiles(value = "test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import({SpringTestConf.class})
class MarkServiceIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.2");
    private static int count = 0;
    @Autowired
    private TeachingRepository teachingRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SchoolClassRepository schoolClassRepository;
    @Autowired
    private TeacherClassRepository teacherClassRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private MarkServiceImpl markService;
    private Teaching teaching;
    private Teacher teacher;
    private Student student;
    private School school;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private AuthenticationService authenticationService;


    @BeforeEach
    @Transactional
    void setUp() {
        var faker = new Faker();
        school = School.builder()
                .code("SS23456")
                .name("Liceo Severi")
                .phoneNumber(432435L)
                .address("Viale L. D’Orsi, 5 80053 - Castellammare di Stabia (NA)")
                .email("naps110002@istruzione.it").build();
        var name = "mario";
        var surname = "pippo" + (char) ('a' + count);
        var password = "Pluto123!";
        var schoolClass = SchoolClass.builder()
                .letter("A")
                .school(school)
                .year(2024)
                .number((short) 1)
                .build();
        schoolRepository.save(school);
        schoolClassRepository.save(schoolClass);
        student = Student.builder()
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
        student = studentRepository.save(student);
        teacher = Teacher.builder()
                .email(faker.internet().emailAddress())
                .name(name)
                .taxId("ABCDEF12G34H567" + (count++))
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
        var subject = new Subject("Storia");
        subjectRepository.save(subject);
        teaching = Teaching.builder()
                .teacher(teacher)
                .id(new TeachingId(teacher.getId(), subject.getTitle()))
                .subjectTitle(subject)
                .typeOfActivity("Lesson")
                .build();
        teachingRepository.save(teaching);
    }

    @Test
    void tc5_01() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.of(2025, 1, 20), "Interrogazione sul Rinascimento", 7.15, MarkType.ORAL);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(teacher, null, List.of(authenticationService.getRole(teacher)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertDoesNotThrow(() -> markService.create(mark));
    }

    @Test
    void tc5_02() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.of(2025, 1, 20), "Interrogazione insufficiente sul Rinascimento", 0.15, MarkType.ORAL);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(teacher, null, List.of(authenticationService.getRole(teacher)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertDoesNotThrow(() -> markService.create(mark));
    }

    @Test
    void tc5_03() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.of(2025, 1, 20), "Ottima Interrogazione sul Rinascimento", 0.15, MarkType.ORAL);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(teacher, null, List.of(authenticationService.getRole(teacher)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertDoesNotThrow(() -> markService.create(mark));
    }

    @Test
    void tc5_04() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.of(2025, 1, 20), "Compito in classe sul Rinascimento", 7.15, MarkType.WRITTEN);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(teacher, null, List.of(authenticationService.getRole(teacher)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertDoesNotThrow(() -> markService.create(mark));
    }

    @Test
    void tc5_05() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.of(2025, 1, 20), "Lettura sul Rinascimento", 7.15, MarkType.LABORATORY);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(teacher, null, List.of(authenticationService.getRole(teacher)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertDoesNotThrow(() -> markService.create(mark));
    }

    @Test
    void tc5_06() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.of(2025, 1, 20), "", 7.15, MarkType.ORAL);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(teacher, null, List.of(authenticationService.getRole(teacher)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertDoesNotThrow(() -> markService.create(mark));
    }

    @Test
    void tc5_07() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.now(), "Interrogazione sul Rinascimento", 7.15, MarkType.WRITTEN);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(teacher, null, List.of(authenticationService.getRole(teacher)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertDoesNotThrow(() -> markService.create(mark));
    }

    @Test
    void tc5_08() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.now().plusDays(1), "Interrogazione sul Rinascimento", 7.15, MarkType.ORAL);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(teacher, null, List.of(authenticationService.getRole(teacher)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertThrows(IllegalArgumentException.class, () -> markService.create(mark));
    }

    @Test
    void tc5_09() {
        var faker = new Faker();
        teaching.setTeacher(Teacher.builder().build());
        var schoolClass = schoolClassRepository.save(SchoolClass.builder()
                .letter("C")
                .school(school)
                .year(2024)
                .number((short) 1)
                .build()
        );
        student = Student.builder()
                .email(faker.internet().emailAddress())
                .name("Luigi")
                .pendingState(PendingState.NORMAL)
                .surname("Rossi")
                .password(Hashing.sha512().hashString("Pluto123!", StandardCharsets.UTF_8).toString())
                .residentialAddress(faker.address().fullAddress())
                .school(school)
                .male(true)
                .username("luigi.rossi")
                .birthDate(LocalDate.of(2003, 5, 22))
                .schoolClasses(Collections.singleton(schoolClass)).build();
        student = studentRepository.save(student);
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.of(2025, 1, 20), "Interrogazione sul Rinascimento", 7.15, MarkType.ORAL);


        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(teacher, null, List.of(authenticationService.getRole(teacher)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertThrows(AccessDeniedException.class, () -> markService.create(mark));
    }

    @Test
    void tc5_10() {
        var faker = new Faker();
        var temp = Teacher.builder()
                .email(faker.internet().emailAddress())
                .name("Luigi")
                .birthDate(LocalDate.of(1990, 5, 22))
                .username("luigi.rossi")
                .male(true)
                .taxId("LBCDEF12G34H5672")
                .residentialAddress(faker.address().fullAddress())
                .pendingState(PendingState.NORMAL)
                .surname("Rossi")
                .password(Hashing.sha512().hashString("Pluto123!", StandardCharsets.UTF_8).toString())
                .school(school).build();
        var subject = new Subject("Matematica");
        subjectRepository.save(subject);
        teaching = Teaching.builder()
                .teacher(temp)
                .id(new TeachingId(teacher.getId(), "Matematica"))
                .subjectTitle(subjectRepository.save(new Subject("Matematica")))
                .typeOfActivity("Lesson")
                .build();
        teacherRepository.save(temp);
        teaching = teachingRepository.save(teaching);
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.of(2025, 1, 20), "Interrogazione sul Rinascimento", 7.15, MarkType.ORAL);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(teacher, null, List.of(authenticationService.getRole(teacher)));
        SecurityContextHolder.getContext().setAuthentication(token);
        assertThrows(AccessDeniedException.class, () -> markService.create(mark));
    }

}