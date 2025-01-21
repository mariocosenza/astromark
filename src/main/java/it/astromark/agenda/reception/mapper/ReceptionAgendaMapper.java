package it.astromark.agenda.reception.mapper;

import it.astromark.agenda.reception.dto.ReceptionBookingResponse;
import it.astromark.agenda.reception.dto.ReceptionTimeslotResponse;
import it.astromark.agenda.reception.entity.ReceptionBooking;
import it.astromark.agenda.reception.entity.ReceptionTimeslot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

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
            @Mapping(target = "name", source = "receptionBooking.receptionTimeslot.receptionTimetable.teacher.name"),
            @Mapping(target = "surname", source = "receptionBooking.receptionTimeslot.receptionTimetable.teacher.surname")
    })
    ReceptionBookingResponse toReceptionBookingResponse(ReceptionBooking receptionBooking);
    List<ReceptionBookingResponse> toReceptionBookingResponseList(List<ReceptionBooking> receptionBookings);

}
