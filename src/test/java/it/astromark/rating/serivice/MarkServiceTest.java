package it.astromark.rating.serivice;

import com.google.common.hash.Hashing;
import it.astromark.SpringTestConf;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.classmanagement.didactic.entity.Subject;
import it.astromark.classmanagement.didactic.entity.Teaching;
import it.astromark.classmanagement.didactic.entity.TeachingId;
import it.astromark.classmanagement.didactic.repository.TeachingRepository;
import it.astromark.classmanagement.entity.SchoolClass;
import it.astromark.classmanagement.entity.TeacherClass;
import it.astromark.classmanagement.entity.TeacherClassId;
import it.astromark.rating.dto.MarkRequest;
import it.astromark.rating.mapper.MarkMapper;
import it.astromark.rating.model.Mark;
import it.astromark.rating.model.MarkType;
import it.astromark.rating.repository.MarkRepository;
import it.astromark.rating.service.MarkServiceImpl;
import it.astromark.school.entity.School;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.entity.Student;
import it.astromark.user.student.repository.StudentRepository;
import it.astromark.user.teacher.entity.Teacher;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ActiveProfiles(value = "test")
@ExtendWith(MockitoExtension.class)
@WithMockUser(roles = "TEACHER")
@Import({SpringTestConf.class})
class MarkServiceTest {
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private TeachingRepository teachingRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private SchoolUserService schoolUserService;
    @Mock
    private MarkRepository markRepository;
    @Mock
    private MarkMapper markMapper;
    @InjectMocks
    private MarkServiceImpl markService;
    private Teaching teaching;
    private Student student;


    @BeforeEach
    public void setUpUser() {
        var faker = new Faker();
        var school = School.builder()
                .code("SS23456")
                .name("Liceo Severi")
                .phoneNumber(432435L)
                .address("Viale L. D’Orsi, 5 80053 - Castellammare di Stabia (NA)")
                .email("naps110002@istruzione.it").build();
        var name = "mario";
        var surname = "pippo";
        var password = "Pluto123!";
        var schoolClass = SchoolClass.builder()
                .letter("A")
                .school(school)
                .number((short) 1)
                .build();
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
                .schoolClasses(Collections.singleton(schoolClass))
                .school(school).build();
        schoolClass.setStudents(Collections.singleton(student));
        var teacher = Teacher.builder()
                .email(faker.internet().emailAddress())
                .name(name)
                .pendingState(PendingState.NORMAL)
                .surname(surname)
                .male(true)
                .username(name + "." + surname)
                .birthDate(LocalDate.of(1990, 5, 22))
                .pendingState(PendingState.NORMAL)
                .school(school)
                .password(Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString())
                .build();
        teacher.setTeacherClasses(Set.of(new TeacherClass(new TeacherClassId(teacher.getId(), schoolClass.getId()), teacher, schoolClass, false)));
        var subject = new Subject("Storia");
        teaching = Teaching.builder()
                .teacher(teacher)
                .id(new TeachingId(teacher.getId(), subject.getTitle()))
                .subjectTitle(subject)
                .typeOfActivity("Lesson")
                .build();
    }

    @Test
    void tc5_01() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.of(2025, 1, 20), "Interrogazione sul Rinascimento", 7.15, MarkType.ORAL);
        when(authenticationService.getTeacher()).thenReturn(Optional.of(teaching.getTeacher()));
        when(teachingRepository.findById(mark.teachingId())).thenReturn(Optional.of(teaching));
        when(studentRepository.findById(mark.studentId())).thenReturn(Optional.of(student));
        when(schoolUserService.isLoggedTeacherStudent(mark.studentId())).thenReturn(true);
        when(markRepository.save(any(Mark.class))).thenReturn(Mark.builder().build());
        when(markMapper.toMarkResponse(any(Mark.class))).thenReturn(null);

        assertDoesNotThrow(() -> markService.create(mark));

        verify(authenticationService).getTeacher();
        verify(teachingRepository).findById(mark.teachingId());
        verify(schoolUserService).isLoggedTeacherStudent(mark.studentId());
        verify(markRepository).save(any(Mark.class));
    }

    @Test
    void tc5_02() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.of(2025, 1, 20), "Interrogazione insufficiente sul Rinascimento", 0.15, MarkType.ORAL);
        when(authenticationService.getTeacher()).thenReturn(Optional.of(teaching.getTeacher()));
        when(teachingRepository.findById(mark.teachingId())).thenReturn(Optional.of(teaching));
        when(studentRepository.findById(mark.studentId())).thenReturn(Optional.of(student));
        when(schoolUserService.isLoggedTeacherStudent(mark.studentId())).thenReturn(true);
        when(markRepository.save(any(Mark.class))).thenReturn(Mark.builder().build());
        when(markMapper.toMarkResponse(any(Mark.class))).thenReturn(null);

        assertDoesNotThrow(() -> markService.create(mark));

        verify(authenticationService).getTeacher();
        verify(teachingRepository).findById(mark.teachingId());
        verify(schoolUserService).isLoggedTeacherStudent(mark.studentId());
        verify(markRepository).save(any(Mark.class));

    }

    @Test
    void tc5_03() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.of(2025, 1, 20), "Ottima Interrogazione sul Rinascimento", 0.15, MarkType.ORAL);
        when(authenticationService.getTeacher()).thenReturn(Optional.of(teaching.getTeacher()));
        when(teachingRepository.findById(mark.teachingId())).thenReturn(Optional.of(teaching));
        when(studentRepository.findById(mark.studentId())).thenReturn(Optional.of(student));
        when(schoolUserService.isLoggedTeacherStudent(mark.studentId())).thenReturn(true);
        when(markRepository.save(any(Mark.class))).thenReturn(Mark.builder().build());
        when(markMapper.toMarkResponse(any(Mark.class))).thenReturn(null);

        assertDoesNotThrow(() -> markService.create(mark));

        verify(authenticationService).getTeacher();
        verify(teachingRepository).findById(mark.teachingId());
        verify(schoolUserService).isLoggedTeacherStudent(mark.studentId());
        verify(markRepository).save(any(Mark.class));
    }

    @Test
    void tc5_04() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.of(2025, 1, 20), "Compito in classe sul Rinascimento", 7.15, MarkType.WRITTEN);
        when(authenticationService.getTeacher()).thenReturn(Optional.of(teaching.getTeacher()));
        when(teachingRepository.findById(mark.teachingId())).thenReturn(Optional.of(teaching));
        when(studentRepository.findById(mark.studentId())).thenReturn(Optional.of(student));
        when(schoolUserService.isLoggedTeacherStudent(mark.studentId())).thenReturn(true);
        when(markRepository.save(any(Mark.class))).thenReturn(Mark.builder().build());
        when(markMapper.toMarkResponse(any(Mark.class))).thenReturn(null);

        assertDoesNotThrow(() -> markService.create(mark));

        verify(authenticationService).getTeacher();
        verify(teachingRepository).findById(mark.teachingId());
        verify(schoolUserService).isLoggedTeacherStudent(mark.studentId());
        verify(markRepository).save(any(Mark.class));
    }

    @Test
    void tc5_05() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.of(2025, 1, 20), "Lettura sul Rinascimento", 7.15, MarkType.LABORATORY);
        when(authenticationService.getTeacher()).thenReturn(Optional.of(teaching.getTeacher()));
        when(teachingRepository.findById(mark.teachingId())).thenReturn(Optional.of(teaching));
        when(studentRepository.findById(mark.studentId())).thenReturn(Optional.of(student));
        when(schoolUserService.isLoggedTeacherStudent(mark.studentId())).thenReturn(true);
        when(markRepository.save(any(Mark.class))).thenReturn(Mark.builder().build());
        when(markMapper.toMarkResponse(any(Mark.class))).thenReturn(null);

        assertDoesNotThrow(() -> markService.create(mark));

        verify(authenticationService).getTeacher();
        verify(teachingRepository).findById(mark.teachingId());
        verify(schoolUserService).isLoggedTeacherStudent(mark.studentId());
        verify(markRepository).save(any(Mark.class));
    }

    @Test
    void tc5_06() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.of(2025, 1, 20), "", 7.15, MarkType.ORAL);
        when(authenticationService.getTeacher()).thenReturn(Optional.of(teaching.getTeacher()));
        when(teachingRepository.findById(mark.teachingId())).thenReturn(Optional.of(teaching));
        when(studentRepository.findById(mark.studentId())).thenReturn(Optional.of(student));
        when(schoolUserService.isLoggedTeacherStudent(mark.studentId())).thenReturn(true);
        when(markRepository.save(any(Mark.class))).thenReturn(Mark.builder().build());
        when(markMapper.toMarkResponse(any(Mark.class))).thenReturn(null);

        assertDoesNotThrow(() -> markService.create(mark));

        verify(authenticationService).getTeacher();
        verify(teachingRepository).findById(mark.teachingId());
        verify(schoolUserService).isLoggedTeacherStudent(mark.studentId());
        verify(markRepository).save(any(Mark.class));
    }

    @Test
    void tc5_07() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.now(), "Interrogazione sul Rinascimento", 7.15, MarkType.WRITTEN);
        when(authenticationService.getTeacher()).thenReturn(Optional.of(teaching.getTeacher()));
        when(teachingRepository.findById(mark.teachingId())).thenReturn(Optional.of(teaching));
        when(studentRepository.findById(mark.studentId())).thenReturn(Optional.of(student));
        when(schoolUserService.isLoggedTeacherStudent(mark.studentId())).thenReturn(true);
        when(markRepository.save(any(Mark.class))).thenReturn(Mark.builder().build());
        when(markMapper.toMarkResponse(any(Mark.class))).thenReturn(null);

        assertDoesNotThrow(() -> markService.create(mark));

        verify(authenticationService).getTeacher();
        verify(teachingRepository).findById(mark.teachingId());
        verify(schoolUserService).isLoggedTeacherStudent(mark.studentId());
        verify(markRepository).save(any(Mark.class));
    }

    @Test
    void tc5_08() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.now().plusDays(1), "Interrogazione sul Rinascimento", 7.15, MarkType.ORAL);

        assertThrows(IllegalArgumentException.class, () -> markService.create(mark));

        verify(authenticationService, times(0)).getTeacher();
        verify(teachingRepository, times(0)).findById(mark.teachingId());
        verify(schoolUserService, times(0)).isLoggedTeacherStudent(mark.studentId());
        verify(markRepository, times(0)).save(any(Mark.class));
    }

    @Test
    void tc5_09() {
        teaching.setTeacher(Teacher.builder().build());
        var mark = new MarkRequest(UUID.randomUUID(), teaching.getId(), LocalDate.of(2025, 1, 20), "Interrogazione sul Rinascimento", 7.15, MarkType.ORAL);
        when(authenticationService.getTeacher()).thenReturn(Optional.of(teaching.getTeacher()));
        when(teachingRepository.findById(mark.teachingId())).thenReturn(Optional.of(teaching));
        when(schoolUserService.isLoggedTeacherStudent(mark.studentId())).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> markService.create(mark));

        verify(authenticationService, times(1)).getTeacher();
        verify(teachingRepository, times(1)).findById(mark.teachingId());
        verify(schoolUserService, times(1)).isLoggedTeacherStudent(mark.studentId());
        verify(markRepository, times(0)).save(any(Mark.class));
    }

    @Test
    void tc5_10() {
        var mark = new MarkRequest(student.getId(), teaching.getId(), LocalDate.of(2025, 1, 20), "Interrogazione sul Rinascimento", 7.15, MarkType.ORAL);
        when(authenticationService.getTeacher()).thenReturn(Optional.of(teaching.getTeacher()));
        teaching.setTeacher(Teacher.builder().id(UUID.randomUUID()).build());
        when(teachingRepository.findById(mark.teachingId())).thenReturn(Optional.of(teaching));

        assertThrows(AccessDeniedException.class, () -> markService.create(mark));

        verify(authenticationService, times(1)).getTeacher();
        verify(teachingRepository, times(1)).findById(mark.teachingId());
        verify(schoolUserService, times(0)).isLoggedTeacherStudent(mark.studentId());
        verify(markRepository, times(0)).save(any(Mark.class));
    }

}