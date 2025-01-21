package it.astromark.agenda.reception.service;

import it.astromark.agenda.reception.dto.ReceptionBookingResponse;
import it.astromark.agenda.reception.dto.ReceptionTimeslotRequest;
import it.astromark.agenda.reception.dto.ReceptionTimeslotResponse;
import it.astromark.agenda.reception.entity.ReceptionBooking;
import it.astromark.agenda.reception.mapper.ReceptionAgendaMapper;
import it.astromark.agenda.reception.repository.ReceptionBookingRepository;
import it.astromark.agenda.reception.repository.ReceptionTimeslotRepository;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.user.parent.service.ParentService;
import it.astromark.user.teacher.entity.Teacher;
import it.astromark.user.teacher.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ReceptionAgendaServiceImpl implements ReceptionAgendaService {

    private final AuthenticationService authenticationService;
    private final TeacherRepository teacherRepository;
    private final ReceptionBookingRepository receptionBookingRepository;
    private final ReceptionTimeslotRepository receptionTimeslotRepository;
    private final ReceptionAgendaMapper receptionAgendaMapper;
    private final ParentService parentService;

    public ReceptionAgendaServiceImpl(AuthenticationService authenticationService, TeacherRepository teacherRepository, ReceptionBookingRepository receptionBookingRepository, ReceptionTimeslotRepository receptionTimeslotRepository, ReceptionAgendaMapper receptionAgendaMapper, ParentService parentService) {
        this.authenticationService = authenticationService;
        this.teacherRepository = teacherRepository;
        this.receptionBookingRepository = receptionBookingRepository;
        this.receptionTimeslotRepository = receptionTimeslotRepository;
        this.receptionAgendaMapper = receptionAgendaMapper;
        this.parentService = parentService;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('parent')")
    public boolean book(Integer receptionTimeslotID) {
        var slot = receptionTimeslotRepository.findByIdAndDateAfter(receptionTimeslotID, LocalDate.now().minusDays(200));
        var parent = authenticationService.getParent().orElseThrow();
        for (var student : parent.getStudents()) {
            if (slot.getReceptionTimetable().getTeacher().getTeacherClasses().stream().anyMatch(c -> c.getSchoolClass().getStudents().contains(student))) {
                if(slot.getBooked() < slot.getCapacity()) {
                    slot.setBooked((short) (slot.getBooked() + 1));
                    receptionBookingRepository.save(ReceptionBooking.builder()
                            .bookingOrder(slot.getBooked())
                            .parent(parent)
                            .confirmed(false)
                            .refused(false)
                            .receptionTimeslot(slot).build());
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean confirm(Integer receptionTimeslotID) {
        return false;
    }

    @Override
    public boolean refuse(Integer receptionTimeslotID) {
        return false;
    }

    @Override
    public ReceptionTimeslotResponse addTimeslot(Integer id, ReceptionTimeslotRequest request) {
        return null;
    }

    @Override
    public List<ReceptionBookingResponse> getNotConfirmed(Integer tableId) {
        return List.of();
    }

    @Override
    public List<ReceptionBookingResponse> getRefused(Integer tableId) {
        return List.of();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('teacher') || hasRole('parent')")
    public List<ReceptionBookingResponse> getBookedSlots() {
        if(authenticationService.isTeacher()) {
            return receptionAgendaMapper.toReceptionBookingResponseList(receptionBookingRepository.findByReceptionTimeslot_ReceptionTimetable_Teacher(authenticationService.getTeacher().orElseThrow()).stream().sorted(Comparator.comparing(a -> a.getReceptionTimeslot().getDate())).toList());
        } else {
            return receptionAgendaMapper.toReceptionBookingResponseList(receptionBookingRepository.findByParent(authenticationService.getParent().orElseThrow()).stream().sorted(Comparator.comparing(a -> a.getReceptionTimeslot().getDate())).toList());
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('teacher') || hasRole('parent')")
    public List<ReceptionTimeslotResponse> getSlots(@NotNull UUID teacherID) {
        Teacher teacher;
        if(authenticationService.isTeacher()) {
            teacher = authenticationService.getTeacher().orElseThrow();
        } else {
            teacher = teacherRepository.findById(teacherID).orElseThrow();
            if(teacher
                    .getTeacherClasses()
                    .stream().anyMatch(c -> c.getSchoolClass().getStudents().stream().noneMatch(s -> parentService.getStudents().stream().anyMatch(p -> p.id().equals(s.getId()))))) {
                throw new AccessDeniedException("Parent is not a student of the teacher");
            }
        }

        var timeslot = receptionTimeslotRepository.findAllByReceptionTimetable_TeacherAndDateAfter(teacher, LocalDate.now().minusDays(200L));

        return timeslot.stream().map(receptionAgendaMapper::toReceptionTimeslotResponse).toList();
    }
}
