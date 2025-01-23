package it.astromark.attendance.mapper;

import it.astromark.attendance.dto.JustifiableResponse;
import it.astromark.attendance.entity.Absence;
import it.astromark.attendance.entity.Delay;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface JustifiableMapper {


    JustifiableResponse toJustifiableResponse(Absence justifiable);

    JustifiableResponse toJustifiableResponse(Delay justifiable);

    List<JustifiableResponse> absenceToJustifiableResponseList(List<Absence> justifiable);

    List<JustifiableResponse> delayToJustifiableResponseList(List<Delay> justifiable);


}
