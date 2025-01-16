package it.astromark.user.commons.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.common.hash.Hashing;
import it.astromark.SpringTestConf;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.school.entity.School;
import it.astromark.user.commons.dto.SchoolUserResponse;
import it.astromark.user.commons.mapper.SchoolUserMapper;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.parent.repository.ParentRepository;
import it.astromark.user.secretary.repository.SecretaryRepository;
import it.astromark.user.student.entity.Student;
import it.astromark.user.student.repository.StudentRepository;
import it.astromark.user.teacher.repository.TeacherRepository;
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
import java.util.Optional;

@Slf4j
@ActiveProfiles(value = "test")
@ExtendWith(MockitoExtension.class)
@WithMockUser(roles="student")
@Import({SpringTestConf.class})
class SchoolUserServiceTest {

    @Mock
    private  AuthenticationService authenticationService;
    @Mock
    private  StudentRepository studentRepository;
    @Mock
    private SchoolUserMapper schoolUserMapper;
    @Mock
    private  TeacherRepository teacherRepository;
    @Mock
    private  SecretaryRepository secretaryRepository;
    @Mock
    private  ParentRepository parentRepository;
    @InjectMocks
    private SchoolUserServiceImpl schoolUserService;

    private static School school;

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
                .pendingState(PendingState.FIRST_LOGIN)
                .surname(surname)
                .password(Hashing.sha512().hashString(faker.internet().password(8, 16, true, true), StandardCharsets.UTF_8).toString()) //unsafe
                .residentialAddress(faker.address().fullAddress())
                .gender(true)
                .birthDate(LocalDate.of(2003, 5, 22))
                .username(name + "." + surname)
                .school(school).build();

    }

    @Test
    void tc2_01() {
        var newAddress = "Via Roma 123";

        when(authenticationService.isStudent()).thenReturn(true);
        when(authenticationService.getStudent()).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student); // Mock save behavior
        when(schoolUserMapper.toSchoolUserResponse(any(Student.class))).thenReturn(new SchoolUserResponse(student.getName(), student.getSurname(), student.getId())); // Mock mapper behavior

        assertDoesNotThrow(() ->  schoolUserService.updateAddress(newAddress)); // Verify address update

        verify(authenticationService, times(1)).isStudent(); // Verify student check
        verify(authenticationService, times(1)).getStudent(); // Verify student retrieval
        verify(studentRepository, times(1)).save(student); // Verify student save
    }

    @Test
    void tc2_02() {
        var newAddress = "Via X";

        when(authenticationService.isStudent()).thenReturn(true);
        when(authenticationService.getStudent()).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student); // Mock save behavior
        when(schoolUserMapper.toSchoolUserResponse(any(Student.class))).thenReturn(new SchoolUserResponse(student.getName(), student.getSurname(), student.getId())); // Mock mapper behavior

        assertDoesNotThrow(() ->  schoolUserService.updateAddress(newAddress)); // Verify address update

        verify(authenticationService, times(1)).isStudent(); // Verify student check
        verify(authenticationService, times(1)).getStudent(); // Verify student retrieval
        verify(studentRepository, times(1)).save(student); // Verify student save
    }

    @Test
    void tc2_03() {
        var newAddress = "Via S. Marco";

        when(authenticationService.isStudent()).thenReturn(true);
        when(authenticationService.getStudent()).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student); // Mock save behavior
        when(schoolUserMapper.toSchoolUserResponse(any(Student.class))).thenReturn(new SchoolUserResponse(student.getName(), student.getSurname(), student.getId())); // Mock mapper behavior

        assertDoesNotThrow(() ->  schoolUserService.updateAddress(newAddress)); // Verify address update

        verify(authenticationService, times(1)).isStudent(); // Verify student check
        verify(authenticationService, times(1)).getStudent(); // Verify student retrieval
        verify(studentRepository, times(1)).save(student); // Verify student save
    }

    @Test
    void tc2_04() {
        var newAddress = "Via";

        assertThrows(IllegalArgumentException.class, () ->  schoolUserService.updateAddress(newAddress)); // Verify address update

        verify(authenticationService, times(0)).isStudent(); // Verify student check
        verify(authenticationService, times(0)).getStudent(); // Verify student retrieval
        verify(studentRepository, times(0)).save(student); // Verify student save
    }

    @Test
    void tc2_05() {
        var newAddress = "Via Napoli#";

        assertThrows(IllegalArgumentException.class, () ->  schoolUserService.updateAddress(newAddress)); // Verify address update

        verify(authenticationService, times(0)).isStudent(); // Verify student check
        verify(authenticationService, times(0)).getStudent(); // Verify student retrieval
        verify(studentRepository, times(0)).save(student); // Verify student save
    }

    @Test
    void tc3_01() {
    }

    @Test
    void tc3_02() {
    }

    @Test
    void tc3_03() {
    }

    @Test
    void tc3_04() {
    }

    @Test
    void tc3_05() {
    }

}