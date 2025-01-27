package it.astromark.agenda.reception.mapper;

import it.astromark.agenda.reception.dto.ReceptionBookingResponse;
import it.astromark.agenda.reception.dto.ReceptionTimeslotResponse;
import it.astromark.agenda.reception.entity.ReceptionBooking;
import it.astromark.agenda.reception.entity.ReceptionTimeslot;
import it.astromark.authentication.service.AuthenticationService;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ReceptionAgendaMapper {

    @Mappings({
            @Mapping(target = "name", source = "receptionTimeslot.receptionTimetable.teacher.name"),
            @Mapping(target = "surname", source = "receptionTimeslot.receptionTimetable.teacher.surname")
    })
    ReceptionTimeslotResponse toReceptionTimeslotResponse(ReceptionTimeslot receptionTimeslot);

    @Mappings({
            @Mapping(target = "date", source = "receptionBooking.receptionTimeslot.date"),
            @Mapping(target = "mode", source = "receptionBooking.receptionTimeslot.mode"),
            @Mapping(target = "hour", source = "receptionBooking.receptionTimeslot.hour"),
            @Mapping(target = "name", expression = "java(getMappedName(receptionBooking, authenticationService))"),
            @Mapping(target = "surname", expression = "java(getMappedSurname(receptionBooking, authenticationService))")
    })
    ReceptionBookingResponse toReceptionBookingResponse(ReceptionBooking receptionBooking, @Context AuthenticationService authenticationService);

    List<ReceptionBookingResponse> toReceptionBookingResponseList(List<ReceptionBooking> receptionBookings, @Context AuthenticationService authenticationService);



    default String getMappedName(ReceptionBooking receptionBooking, AuthenticationService authenticationService) {
        if (authenticationService.isParent()) {
            return receptionBooking.getReceptionTimeslot().getReceptionTimetable().getTeacher().getName();
        } else if (authenticationService.isTeacher()) {
            return receptionBooking.getParent().getName();
        }
        return null;
    }

    default String getMappedSurname(ReceptionBooking receptionBooking, AuthenticationService authenticationService) {
        if (authenticationService.isParent()) {
            return receptionBooking.getReceptionTimeslot().getReceptionTimetable().getTeacher().getSurname();
        } else if (authenticationService.isTeacher()) {
            return receptionBooking.getParent().getSurname();
        }
        return null;
    }
}
