package it.astromark.classwork.mapper;

import it.astromark.classwork.dto.ClassworkResponse;
import it.astromark.classwork.entity.ClassActivity;
import it.astromark.classwork.entity.Homework;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ClassworkMapper {


    @Mapping(target = "signedHour", source = "classwork.signedHour.teachingTimeslot")
    ClassworkResponse toClassworkResponse(ClassActivity classwork);
    @Mapping(target = "signedHour", source = "classwork.signedHour.teachingTimeslot")
    ClassworkResponse toClassworkResponse(Homework classwork);

    List<ClassworkResponse> classActivityToClassworkResponseList(List<ClassActivity> classworks);
    List<ClassworkResponse> homeworkToClassworkResponseList(List<Homework> classworks);


}
