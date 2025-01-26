package it.astromark.classwork.mapper;

import it.astromark.classwork.dto.ClassworkResponse;
import it.astromark.classwork.dto.HomeworkResponse;
import it.astromark.classwork.entity.ClassActivity;
import it.astromark.classwork.entity.Homework;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClassworkMapper {


    @Mappings({
            @Mapping(target = "signedHour", source = "classwork.signedHour.teachingTimeslot"),
            @Mapping(target = "signedHour.title", source = "classwork.signedHour.teachingTimeslot.teaching.subjectTitle.title"),
            @Mapping(target = "signedHour.hour", source = "classwork.signedHour.teachingTimeslot.hour"),
            @Mapping(target = "signedHour.date", source = "classwork.signedHour.teachingTimeslot.date"),
    })
    ClassworkResponse toClassworkResponse(ClassActivity classwork);

    @Mappings({
            @Mapping(target = "signedHour", source = "homework.signedHour.teachingTimeslot"),
            @Mapping(target = "signedHour.title", source = "homework.signedHour.teachingTimeslot.teaching.subjectTitle.title"),
            @Mapping(target = "signedHour.hour", source = "homework.signedHour.teachingTimeslot.hour"),
            @Mapping(target = "signedHour.date", source = "homework.signedHour.teachingTimeslot.date"),
            @Mapping(target = "chat", expression = "java(homework.getHomeworkChats().size() > 0)")
    })
    HomeworkResponse toHomeworkResponse(Homework homework);

    List<ClassworkResponse> classActivityToClassworkResponseList(List<ClassActivity> classworks);

    List<HomeworkResponse> homeworkToHomeworkResponseList(List<Homework> classworks);


}
