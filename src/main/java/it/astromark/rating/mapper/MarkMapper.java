package it.astromark.rating.mapper;

import it.astromark.rating.dto.*;
import it.astromark.rating.model.Mark;
import it.astromark.rating.model.SemesterReport;
import it.astromark.rating.model.SemesterReportMark;
import org.mapstruct.Context;
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

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "name", source = "student.name")
    @Mapping(target = "surname", source = "student.surname")
    @Mapping(target = "subject", expression = "java(subject)")
    RatingsResponse toRatingsResponse(Mark mark, @Context String subject);

    List<RatingsResponse> toRatingsResponseList(List<Mark> marks, @Context String subject);

    @Mapping(target = "title", source = "teaching.id.subjectTitle")
    MarkOrientationRequest toMarkOrientationRequest(Mark mark);

    List<MarkOrientationRequest> toMarkOrientationRequestList(List<Mark> marks);

}
