package it.astromark.user.commons.service;

import com.google.common.hash.Hashing;
import it.astromark.SpringTestConf;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.school.entity.School;
import it.astromark.user.commons.dto.SchoolUserResponse;
import it.astromark.user.commons.dto.SchoolUserUpdate;
import it.astromark.user.commons.mapper.SchoolUserMapper;
import it.astromark.user.commons.model.PendingState;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
@ActiveProfiles(value = "test")
@ExtendWith(MockitoExtension.class)
@WithMockUser(roles = "STUDENT")
@Import({SpringTestConf.class})
class SchoolUserServiceTest {

    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private SchoolUserMapper schoolUserMapper;
    @InjectMocks
    private SchoolUserServiceImpl schoolUserService;

    private Student student;

    @BeforeEach
    public void setUpUser() {
        School school = School.builder()
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
    void tc2_01() {
        var newAddress = "Via Roma 123";

        when(authenticationService.isStudent()).thenReturn(true);
        when(authenticationService.getStudent()).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student); // Mock save behavior
        when(schoolUserMapper.toSchoolUserResponse(any(Student.class))).thenReturn(new SchoolUserResponse(student.getName(), student.getSurname(), student.getId())); // Mock mapper behavior

        assertDoesNotThrow(() -> schoolUserService.updateAddress(newAddress));

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

        assertDoesNotThrow(() -> schoolUserService.updateAddress(newAddress)); // Verify address update

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

        assertDoesNotThrow(() -> schoolUserService.updateAddress(newAddress)); // Verify address update

        verify(authenticationService, times(1)).isStudent(); // Verify student check
        verify(authenticationService, times(1)).getStudent(); // Verify student retrieval
        verify(studentRepository, times(1)).save(student); // Verify student save
    }

    @Test
    void tc2_04() {
        var newAddress = "Via";

        assertThrows(IllegalArgumentException.class, () -> schoolUserService.updateAddress(newAddress)); // Verify address update

        verify(authenticationService, times(0)).isStudent(); // Verify student check
        verify(authenticationService, times(0)).getStudent(); // Verify student retrieval
        verify(studentRepository, times(0)).save(student); // Verify student save
    }

    @Test
    void tc2_05() {
        var newAddress = "Via Napoli#";

        assertThrows(IllegalArgumentException.class, () -> schoolUserService.updateAddress(newAddress)); // Verify address update

        verify(authenticationService, times(0)).isStudent(); // Verify student check
        verify(authenticationService, times(0)).getStudent(); // Verify student retrieval
        verify(studentRepository, times(0)).save(student); // Verify student save
    }

    @Test
    void tc3_01() {
        when(authenticationService.isStudent()).thenReturn(true);
        when(authenticationService.getStudent()).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student); // Mock save behavior
        when(schoolUserMapper.toSchoolUserResponse(any(Student.class))).thenReturn(new SchoolUserResponse(student.getName(), student.getSurname(), student.getId())); // Mock mapper behavior

        assertDoesNotThrow(() -> schoolUserService.updatePreferences(new SchoolUserUpdate("Pluto123!"))); // Verify password update

        verify(authenticationService, times(1)).isStudent(); // Verify student check
        verify(authenticationService, times(1)).getStudent(); // Verify student retrieval
        verify(studentRepository, times(1)).save(student); // Verify student save
    }

    @Test
    void tc3_02() {
        when(authenticationService.isStudent()).thenReturn(true);
        when(authenticationService.getStudent()).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student); // Mock save behavior
        when(schoolUserMapper.toSchoolUserResponse(any(Student.class))).thenReturn(new SchoolUserResponse(student.getName(), student.getSurname(), student.getId())); // Mock mapper behavior

        assertDoesNotThrow(() -> schoolUserService.updatePreferences(new SchoolUserUpdate("Pluto12!"))); // Verify password update

        verify(authenticationService, times(1)).isStudent(); // Verify student check
        verify(authenticationService, times(1)).getStudent(); // Verify student retrieval
        verify(studentRepository, times(1)).save(student); // Verify student save
    }

    @Test
    void tc3_03() {
        assertThrows(IllegalArgumentException.class, () -> schoolUserService.updatePreferences(new SchoolUserUpdate("Pluto1!"))); // Verify password update

        verify(authenticationService, times(0)).isStudent(); // Verify student check
        verify(authenticationService, times(0)).getStudent(); // Verify student retrieval
        verify(studentRepository, times(0)).save(student); // Verify student save
    }

    @Test
    void tc3_04() {
        assertThrows(IllegalArgumentException.class, () -> schoolUserService.updatePreferences(new SchoolUserUpdate("Pluto1234"))); // Verify password update

        verify(authenticationService, times(0)).isStudent(); // Verify student check
        verify(authenticationService, times(0)).getStudent(); // Verify student retrieval
        verify(studentRepository, times(0)).save(student); // Verify student save
    }

    @Test
    void tc3_05() {
        //This test is not applicable for the backend
    }

}