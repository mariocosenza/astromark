package it.astromark.agenda.reception.service;

import com.google.common.hash.Hashing;
import it.astromark.SpringTestConf;
import it.astromark.agenda.reception.entity.ReceptionBooking;
import it.astromark.agenda.reception.entity.ReceptionBookingId;
import it.astromark.agenda.reception.entity.ReceptionTimeslot;
import it.astromark.agenda.reception.entity.ReceptionTimetable;
import it.astromark.agenda.reception.repository.ReceptionBookingRepository;
import it.astromark.agenda.reception.repository.ReceptionTimeslotRepository;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.classmanagement.entity.SchoolClass;
import it.astromark.classmanagement.entity.TeacherClass;
import it.astromark.classmanagement.entity.TeacherClassId;
import it.astromark.school.entity.School;
import it.astromark.user.commons.model.PendingState;
import it.astromark.user.parent.entity.Parent;
import it.astromark.user.student.entity.Student;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@Slf4j
@ActiveProfiles(value = "test")
@ExtendWith(MockitoExtension.class)
@WithMockUser(roles = "PARENT")
@Import({SpringTestConf.class})
class ReceptionAgendaServiceTest {

    @Mock
    private ReceptionTimeslotRepository receptionTimeslotRepository;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private ReceptionBookingRepository receptionBookingRepository;
    @InjectMocks
    private ReceptionAgendaServiceImpl receptionAgendaService;
    private Parent parent;
    private School sc;
    private ReceptionTimetable table;

    @BeforeEach
    void setUp() {
        var faker = new Faker();
        var school = School.builder()
                .code("SS23456")
                .name("Liceo Severi")
                .phoneNumber(432435L)
                .address("Viale L. D’Orsi, 5 80053 - Castellammare di Stabia (NA)")
                .email("naps110002@istruzione.it").build();
        var name = "pluto";
        var surname = "pippo";
        var password = "Pluto123!";
        var schoolClass = SchoolClass.builder()
                .letter("A")
                .school(school)
                .number((short) 1)
                .build();
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
                .schoolClasses(Collections.singleton(schoolClass))
                .school(school).build();
        schoolClass.setStudents(Collections.singleton(student));
        parent = Parent.builder()
                .email(faker.internet().emailAddress())
                .id(UUID.randomUUID())
                .name(name)
                .pendingState(PendingState.NORMAL)
                .surname(surname)
                .male(true)
                .username(name + "." + surname)
                .birthDate(LocalDate.of(1990, 5, 22))
                .password(Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString()) //unsafe
                .residentialAddress(faker.address().fullAddress())
                .student(student)
                .build();
        Teacher teacher = Teacher.builder()
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
        table = ReceptionTimetable.builder()
                .id(1)
                .startValidity(LocalDate.now().minusDays(200))
                .teacher(teacher)
                .build();
    }

    @Test
    void tc4_01() {
        var slot = ReceptionTimeslot
                .builder().id(1).date(LocalDate.now()).mode("In Presenza").hour((short) 1).receptionTimetable(table).booked((short) 4).capacity((short) 6).build();

        when(authenticationService.getParent()).thenReturn(Optional.of(parent));
        when(receptionTimeslotRepository.findByIdAndDateAfter(1, LocalDate.now())).thenReturn(slot);
        when(receptionBookingRepository.save(ReceptionBooking.builder().bookingOrder(slot.getBooked()).id(new ReceptionBookingId(parent.getId(), slot.getId())).parent(parent).confirmed(false).refused(false).receptionTimeslot(slot).build())).thenReturn(null);

        assertTrue(receptionAgendaService.book(1));

        verify(receptionBookingRepository, times(1)).save(ReceptionBooking.builder().bookingOrder(slot.getBooked()).id(new ReceptionBookingId(parent.getId(), slot.getId())).parent(parent).confirmed(false).refused(false).receptionTimeslot(slot).build());
        verify(authenticationService).getParent();
    }

    @Test
    void tc4_02() {
        var slot = ReceptionTimeslot
                .builder().id(1).date(LocalDate.now()).mode("Online").hour((short) 1).receptionTimetable(table).booked((short) 4).capacity((short) 6).build();

        when(authenticationService.getParent()).thenReturn(Optional.of(parent));
        when(receptionTimeslotRepository.findByIdAndDateAfter(1, LocalDate.now())).thenReturn(slot);
        when(receptionBookingRepository.save(ReceptionBooking.builder().bookingOrder(slot.getBooked()).id(new ReceptionBookingId(parent.getId(), slot.getId())).parent(parent).confirmed(false).refused(false).receptionTimeslot(slot).build())).thenReturn(null);

        assertTrue(receptionAgendaService.book(1));

        verify(receptionBookingRepository, times(1)).save(ReceptionBooking.builder().bookingOrder(slot.getBooked()).id(new ReceptionBookingId(parent.getId(), slot.getId())).parent(parent).confirmed(false).refused(false).receptionTimeslot(slot).build());
        verify(authenticationService).getParent();
    }

    @Test
    void tc4_03() {
        var slot = ReceptionTimeslot
                .builder().id(1).date(LocalDate.now()).mode("In Presenza").hour((short) 1).receptionTimetable(table).booked((short) 0).capacity((short) 6).build();

        when(authenticationService.getParent()).thenReturn(Optional.of(parent));
        when(receptionTimeslotRepository.findByIdAndDateAfter(1, LocalDate.now())).thenReturn(slot);
        when(receptionBookingRepository.save(ReceptionBooking.builder().bookingOrder(slot.getBooked()).id(new ReceptionBookingId(parent.getId(), slot.getId())).parent(parent).confirmed(false).refused(false).receptionTimeslot(slot).build())).thenReturn(null);

        assertTrue(receptionAgendaService.book(1));

        verify(receptionBookingRepository, times(1)).save(ReceptionBooking.builder().bookingOrder(slot.getBooked()).id(new ReceptionBookingId(parent.getId(), slot.getId())).parent(parent).confirmed(false).refused(false).receptionTimeslot(slot).build());
        verify(authenticationService).getParent();
    }

    @Test
    void tc4_04() {
        var slot = ReceptionTimeslot
                .builder().id(1).date(LocalDate.now()).mode("In Presenza").hour((short) 1).receptionTimetable(table).booked((short) 0).capacity((short) 1).build();

        when(authenticationService.getParent()).thenReturn(Optional.of(parent));
        when(receptionTimeslotRepository.findByIdAndDateAfter(1, LocalDate.now())).thenReturn(slot);
        when(receptionBookingRepository.save(ReceptionBooking.builder().bookingOrder(slot.getBooked()).id(new ReceptionBookingId(parent.getId(), slot.getId())).parent(parent).confirmed(false).refused(false).receptionTimeslot(slot).build())).thenReturn(null);

        assertTrue(receptionAgendaService.book(1));

        verify(receptionBookingRepository, times(1)).save(ReceptionBooking.builder().bookingOrder(slot.getBooked()).id(new ReceptionBookingId(parent.getId(), slot.getId())).parent(parent).confirmed(false).refused(false).receptionTimeslot(slot).build());
        verify(authenticationService).getParent();
    }

    @Test
    void tc4_05() {
        var slot = ReceptionTimeslot
                .builder().id(1).date(LocalDate.now()).mode("In Presenza").hour((short) 1).receptionTimetable(table).booked((short) 6).capacity((short) 6).build();

        when(authenticationService.getParent()).thenReturn(Optional.of(parent));
        when(receptionTimeslotRepository.findByIdAndDateAfter(1, LocalDate.now())).thenReturn(slot);

        assertFalse(receptionAgendaService.book(1));

        verify(receptionBookingRepository, times(0)).save(ReceptionBooking.builder().bookingOrder(slot.getBooked()).id(new ReceptionBookingId(parent.getId(), slot.getId())).parent(parent).confirmed(false).refused(false).receptionTimeslot(slot).build());
        verify(authenticationService).getParent();
    }

    @Test
    void tc4_06() {
        var slot = ReceptionTimeslot
                .builder().id(1).date(LocalDate.now()).mode("In Presenza").hour((short) 1).receptionTimetable(table).booked((short) 1).capacity((short) 1).build();

        when(authenticationService.getParent()).thenReturn(Optional.of(parent));
        when(receptionTimeslotRepository.findByIdAndDateAfter(1, LocalDate.now())).thenReturn(slot);

        assertFalse(receptionAgendaService.book(1));

        verify(receptionBookingRepository, times(0)).save(ReceptionBooking.builder().bookingOrder(slot.getBooked()).id(new ReceptionBookingId(parent.getId(), slot.getId())).parent(parent).confirmed(false).refused(false).receptionTimeslot(slot).build());
        verify(authenticationService).getParent();
    }

}