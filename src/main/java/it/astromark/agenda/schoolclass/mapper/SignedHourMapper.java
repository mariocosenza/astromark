package it.astromark.agenda.schoolclass.mapper;

import it.astromark.agenda.schoolclass.dto.SignHourResponse;
import it.astromark.agenda.schoolclass.entity.TeachingTimeslot;
import it.astromark.classwork.repository.ClassActivityRepository;
import it.astromark.classwork.repository.HomeworkRepository;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SignedHourMapper {

    @Mapping(target = "name", source = "teaching.teacher.name")
    @Mapping(target = "surname", source = "teaching.teacher.surname")
    @Mapping(target = "subject", source = "teaching.subjectTitle.title")
    @Mapping(target = "activityTitle", source = "teachingTimeslot", qualifiedByName = "getActivityTitle")
    @Mapping(target = "activityDescription", source = "teachingTimeslot", qualifiedByName = "getActivityDesc")
    @Mapping(target = "homeworkTitle", source = "teachingTimeslot", qualifiedByName = "getHomeworkTitle")
    @Mapping(target = "homeworkDescription", source = "teachingTimeslot", qualifiedByName = "getHomeworkDesc")
    SignHourResponse toSignedHourResponse(TeachingTimeslot teachingTimeslot, @Context ClassActivityRepository classActivityRepository, @Context HomeworkRepository homeworkRepository);

    List<SignHourResponse> toSignedHourResponseList(List<TeachingTimeslot> teachingTimeslot, @Context ClassActivityRepository classActivityRepository, @Context HomeworkRepository homeworkRepository);


    @Named("getActivityTitle")
    default String getActivityTitle(TeachingTimeslot teachingTimeslot, @Context ClassActivityRepository classActivityRepository) {
        var signHour = teachingTimeslot.getSignedHour();
        if (signHour != null) {
            var classActivity = classActivityRepository.findBySignedHour(signHour);
            if (classActivity != null) {
                return classActivity.getTitle();
            }
        }
        return "";
    }

    @Named("getActivityDesc")
    default String getActivityDesc(TeachingTimeslot teachingTimeslot, @Context ClassActivityRepository classActivityRepository) {
        var signHour = teachingTimeslot.getSignedHour();
        if (signHour != null) {
            var classActivity = classActivityRepository.findBySignedHour(signHour);
            if (classActivity != null) {
                return classActivity.getDescription();
            }
        }
        return "";
    }

    @Named("getHomeworkTitle")
    default String getHomeworkTitle(TeachingTimeslot teachingTimeslot, @Context HomeworkRepository homeworkRepository) {
        var signHour = teachingTimeslot.getSignedHour();
        if (signHour != null) {
            var homework = homeworkRepository.findBySignedHour(signHour);
            if (homework != null) {
                return homework.getTitle();
            }
        }
        return "";
    }

    @Named("getHomeworkDesc")
    default String getHomeworkDesc(TeachingTimeslot teachingTimeslot, @Context HomeworkRepository homeworkRepository) {
        var signHour = teachingTimeslot.getSignedHour();
        if (signHour != null) {
            var homework = homeworkRepository.findBySignedHour(signHour);
            if (homework != null) {
                return homework.getDescription();
            }
        }
        return "";
    }



}

