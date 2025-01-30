package it.astromark.agenda.schoolclass.mapper;

import it.astromark.agenda.schoolclass.dto.TeachingTimeslotDetailedResponse;
import it.astromark.agenda.schoolclass.entity.TeachingTimeslot;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassAgendaMapper {

    @Mapping(target = "subject", source = "teaching.subjectTitle.title")
    @Mapping(target = "signed", expression = "java(teachingTimeslot.getSignedHour() != null)")
    @Mapping(target = "teacher", expression = "java(classAgendaHelperMapper.mapTeacher(teachingTimeslot.getTeaching().getTeacher()))")
    @Mapping(target = "activity", expression = "java(classAgendaHelperMapper.mapActivity(teachingTimeslot))")
    @Mapping(target = "homework", expression = "java(classAgendaHelperMapper.mapHomework(teachingTimeslot))")
    TeachingTimeslotDetailedResponse toTeachingTimeslotDetailedResponse(TeachingTimeslot teachingTimeslot, @Context ClassAgendaHelperMapper classAgendaHelperMapper);

    List<TeachingTimeslotDetailedResponse> toTeachingTimeslotDetailedResponseList(List<TeachingTimeslot> teachingTimeslots, @Context ClassAgendaHelperMapper classAgendaHelperMapper);
}
