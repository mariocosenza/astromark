package it.astromark.user.commons.mapper;

import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.dto.SchoolUserResponse;
import it.astromark.user.commons.model.SchoolUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface SchoolUserMapper {
    SchoolUserResponse toSchoolUserResponse(SchoolUser student);
    SchoolUserDetailed toSchoolUserDetailed(SchoolUser student);
}
