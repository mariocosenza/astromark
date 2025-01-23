package it.astromark.rating.serivice;

import com.google.common.hash.Hashing;
import it.astromark.SpringTestConf;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.school.entity.School;
import it.astromark.user.commons.mapper.SchoolUserMapper;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.commons.service.SchoolUserServiceImpl;
import it.astromark.user.student.entity.Student;
import it.astromark.user.student.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Slf4j
@ActiveProfiles(value = "test")
@ExtendWith(MockitoExtension.class)
@WithMockUser(roles="STUDENT")
@Import({SpringTestConf.class})
class MarkServiceTest {
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private SchoolUserMapper schoolUserMapper;
    @InjectMocks
    private SchoolUserServiceImpl schoolUserService;

    private School school;

    private Student student;

    @BeforeEach
    public void setUpUser() {
        school = School.builder()
                .code("SS23456")
                .name("Liceo Severi")
                .phoneNumber(432435L)
                .address("Viale L. D’Orsi, 5 80053 - Castellammare di Stabia (NA)")
                .email("naps110002@istruzione.it").build();
        var faker = new Faker();
        var name = faker.name().firstName();
        var surname = faker.name().lastName();
        student = Student.builder()
                .email(faker.internet().emailAddress())
                .name(name)
                .pendingState(PendingState.NORMAL)
                .surname(surname)
                .password(Hashing.sha512().hashString(faker.internet().password(8, 16, true, true), StandardCharsets.UTF_8).toString()) //unsafe
                .residentialAddress(faker.address().fullAddress())
                .male(true)
                .birthDate(LocalDate.of(2003, 5, 22))
                .username(name + "." + surname)
                .school(school).build();
    }

    @Test
    void tc5_01() {

    }

    @Test
    void tc2_02() {

    }

    @Test
    void tc5_03() {

    }

    @Test
    void tc5_04() {

    }

    @Test
    void tc5_05() {

    }

    @Test
    void tc5_06() {

    }

    @Test
    void tc5_07() {

    }


}