package it.astromark.classmanagement.didactic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ClassManagementMapper {
   // TeachingResponseNoId toTeachingResponseNoId(Teaching mark);


}
