package it.astromark.agenda.reception.service;

import it.astromark.agenda.reception.dto.ReceptionBookingResponse;
import it.astromark.agenda.reception.dto.ReceptionTimeslotRequest;
import it.astromark.agenda.reception.dto.ReceptionTimeslotResponse;
import it.astromark.agenda.reception.entity.ReceptionBooking;
import it.astromark.agenda.reception.entity.ReceptionBookingId;
import it.astromark.agenda.reception.entity.ReceptionTimeslot;
import it.astromark.agenda.reception.mapper.ReceptionAgendaMapper;
import it.astromark.agenda.reception.repository.ReceptionBookingRepository;
import it.astromark.agenda.reception.repository.ReceptionTimeslotRepository;
import it.astromark.agenda.reception.repository.ReceptionTimetableRepository;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.user.teacher.entity.Teacher;
import it.astromark.user.teacher.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ReceptionAgendaServiceImpl implements ReceptionAgendaService {

    private final AuthenticationService authenticationService;
    private final TeacherRepository teacherRepository;
    private final ReceptionBookingRepository receptionBookingRepository;
    private final ReceptionTimeslotRepository receptionTimeslotRepository;
    private final ReceptionAgendaMapper receptionAgendaMapper;
    private final ReceptionTimetableRepository receptionTimetableRepository;

    public ReceptionAgendaServiceImpl(AuthenticationService authenticationService, TeacherRepository teacherRepository, ReceptionBookingRepository receptionBookingRepository, ReceptionTimeslotRepository receptionTimeslotRepository, ReceptionAgendaMapper receptionAgendaMapper, ReceptionTimetableRepository receptionTimetableRepository) {
        this.authenticationService = authenticationService;
        this.teacherRepository = teacherRepository;
        this.receptionBookingRepository = receptionBookingRepository;
        this.receptionTimeslotRepository = receptionTimeslotRepository;
        this.receptionAgendaMapper = receptionAgendaMapper;
        this.receptionTimetableRepository = receptionTimetableRepository;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('PARENT')")
    public boolean book(Integer receptionTimeslotID) {
        var slot = receptionTimeslotRepository.findByIdAndDateAfter(receptionTimeslotID, LocalDate.now());
        var parent = authenticationService.getParent().orElseThrow();
        for (var student : parent.getStudents()) {
            if (slot.getReceptionTimetable().getTeacher().getTeacherClasses().stream().anyMatch(c -> c.getSchoolClass().getStudents().contains(student))) {
                if (slot.getBooked() < slot.getCapacity()) {
                    slot.setBooked((short) (slot.getBooked() + 1));
                    receptionBookingRepository.save(ReceptionBooking.builder()
                            .bookingOrder(slot.getBooked())
                            .id(new ReceptionBookingId(parent.getId(), receptionTimeslotID))
                            .parent(parent)
                            .confirmed(false)
                            .refused(false)
                            .receptionTimeslot(slot).build());
                    receptionTimeslotRepository.save(slot);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean confirm(ReceptionBookingId receptionTimeslotID) {
        return false;
    }

    @Override
    public boolean refuse(ReceptionBookingId receptionTimeslotID) {
        return false;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public ReceptionTimeslotResponse addTimeslot(ReceptionTimeslotRequest request) {

        var timetable = receptionTimetableRepository.findByEndValidityAndTeacher(null, authenticationService.getTeacher().orElseThrow());

        if (timetable == null)
            return null;

        return receptionAgendaMapper.toReceptionTimeslotResponse(
                receptionTimeslotRepository.save(ReceptionTimeslot.builder()
                        .receptionTimetable(timetable)
                        .date(request.date())
                        .hour(request.hour())
                        .booked((short) 0)
                        .capacity(request.capacity())
                        .mode(request.mode())
                        .build()
                ));
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
    @PreAuthorize("hasRole('TEACHER') || hasRole('PARENT')")
    public List<ReceptionBookingResponse> getBookedSlots() {
        List<ReceptionBooking> booking;
        if (authenticationService.isTeacher()) {
            booking = receptionBookingRepository.findByReceptionTimeslot_ReceptionTimetable_Teacher(authenticationService.getTeacher().orElseThrow());
        } else {
            booking = receptionBookingRepository.findByParent(authenticationService.getParent().orElseThrow());
        }

        return receptionAgendaMapper.toReceptionBookingResponseList(booking.stream()
                .sorted(Comparator.comparing((ReceptionBooking b) -> b.getReceptionTimeslot().getDate())
                        .thenComparing(b -> b.getReceptionTimeslot().getHour())
                        .thenComparing(ReceptionBooking::getBookingOrder))
                .toList(), authenticationService);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER') || hasRole('PARENT')")
    public List<ReceptionTimeslotResponse> getSlots(@NotNull UUID teacherID) {
        Teacher teacher;
        if (authenticationService.isTeacher()) {
            teacher = authenticationService.getTeacher().orElseThrow();
        } else {
            teacher = teacherRepository.findById(teacherID).orElseThrow();
        }

        var timeslot = receptionTimeslotRepository.findAllByReceptionTimetable_TeacherAndDateAfter(teacher, LocalDate.now());

        return timeslot.stream().map(receptionAgendaMapper::toReceptionTimeslotResponse).toList();
    }
}
