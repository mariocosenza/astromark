package it.astromark.rating.mapper;

import it.astromark.rating.dto.MarkResponse;
import it.astromark.rating.model.Mark;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MarkMapper {

    @Mapping(target = "title", source = "teaching.id.subjectTitle")
    MarkResponse toMarkResponse(Mark mark);

    List<MarkResponse> toMarkResponseList(List<Mark> marks);

}
