package it.astromark.agenda.commons.mapper;

import it.astromark.agenda.schoolclass.dto.TeachingTimeslotResponse;
import it.astromark.agenda.schoolclass.entity.TeachingTimeslot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TimeslotMapper {

    @Mapping(target = "title", source = "timeslot.teaching.subjectTitle.title")
    TeachingTimeslotResponse toTeachingTimeslotResponse(TeachingTimeslot timeslot);
    List<TeachingTimeslotResponse> toTeachingTimeslotResponseList(List<TeachingTimeslot> timeslots);
}
