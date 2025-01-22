package it.astromark.rating.mapper;

import it.astromark.rating.dto.MarkResponse;
import it.astromark.rating.dto.SemesterReportMarkResponse;
import it.astromark.rating.dto.SemesterReportResponse;
import it.astromark.rating.model.Mark;
import it.astromark.rating.model.SemesterReport;
import it.astromark.rating.model.SemesterReportMark;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MarkMapper {

    @Mapping(target = "title", source = "teaching.id.subjectTitle")
    MarkResponse toMarkResponse(Mark mark);

    List<MarkResponse> toMarkResponseList(List<Mark> marks);

    @Mapping(target = "title", source = "subjectTitle.title")
    SemesterReportMarkResponse toSemesterReportMarkResponse(SemesterReportMark mark);

    List<SemesterReportMarkResponse> toSemesterReportMarkResponseList(List<SemesterReportMark> marks);

    Set<SemesterReportMarkResponse> toSemesterReportMarkResponseSet(Set<SemesterReportMark> marks);

    SemesterReportResponse toSemesterReportResponse(SemesterReport report);

    List<SemesterReportResponse> toSemesterReportResponseList(List<SemesterReport> reports);


}
