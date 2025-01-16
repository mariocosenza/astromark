package it.astromark.agenda.schoolclass.mapper;

import it.astromark.agenda.schoolclass.dto.TeachingTimeslotDetailedResponse;
import it.astromark.agenda.schoolclass.entity.TeachingTimeslot;
import it.astromark.classwork.repository.ClassActivityRepository;
import it.astromark.classwork.repository.HomeworkRepository;
import org.mapstruct.*;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassAgendaMapper {

    @Mapping(target = "teacherId", source = "teaching.teacher.id")
    @Mapping(target = "name", source = "teaching.teacher.name")
    @Mapping(target = "surname", source = "teaching.teacher.surname")
    @Mapping(target = "subject", source = "teaching.subjectTitle.title")
    @Mapping(target = "signed", source = "teachingTimeslot", qualifiedByName = "isSigned")
    @Mapping(target = "activityTitle", source = "teachingTimeslot", qualifiedByName = "getActivityTitle")
    @Mapping(target = "activityDescription", source = "teachingTimeslot", qualifiedByName = "getActivityDesc")
    @Mapping(target = "homeworkTitle", source = "teachingTimeslot", qualifiedByName = "getHomeworkTitle")
    @Mapping(target = "homeworkDescription", source = "teachingTimeslot", qualifiedByName = "getHomeworkDesc")
    @Mapping(target = "homeworkDueDate", source = "teachingTimeslot", qualifiedByName = "getHomeworkDate")
    TeachingTimeslotDetailedResponse toTeachingTimeslotDetailedResponse(TeachingTimeslot teachingTimeslot, @Context ClassActivityRepository classActivityRepository, @Context HomeworkRepository homeworkRepository);

    List<TeachingTimeslotDetailedResponse> toTeachingTimeslotDetailedResponseList(List<TeachingTimeslot> teachingTimeslot, @Context ClassActivityRepository classActivityRepository, @Context HomeworkRepository homeworkRepository);


    @Named("isSigned")
    default boolean isSigned(TeachingTimeslot teachingTimeslot) {
        return teachingTimeslot.getSignedHour() != null;
    }

    @Named("getActivityTitle")
    default String getActivityTitle(TeachingTimeslot teachingTimeslot, @Context ClassActivityRepository classActivityRepository) {
        if (isSigned(teachingTimeslot)) {
            var classActivity = classActivityRepository.findBySignedHour(teachingTimeslot.getSignedHour());
            if (classActivity != null) {
                return classActivity.getTitle();
            }
        }
        return "";
    }

    @Named("getActivityDesc")
    default String getActivityDesc(TeachingTimeslot teachingTimeslot, @Context ClassActivityRepository classActivityRepository) {
        if (isSigned(teachingTimeslot)) {
            var classActivity = classActivityRepository.findBySignedHour(teachingTimeslot.getSignedHour());
            if (classActivity != null) {
                return classActivity.getDescription();
            }
        }
        return "";
    }

    @Named("getHomeworkTitle")
    default String getHomeworkTitle(TeachingTimeslot teachingTimeslot, @Context HomeworkRepository homeworkRepository) {
        if (isSigned(teachingTimeslot)) {
            var homework = homeworkRepository.findBySignedHour(teachingTimeslot.getSignedHour());
            if (homework != null) {
                return homework.getTitle();
            }
        }
        return "";
    }

    @Named("getHomeworkDesc")
    default String getHomeworkDesc(TeachingTimeslot teachingTimeslot, @Context HomeworkRepository homeworkRepository) {
        if (isSigned(teachingTimeslot)) {
            var homework = homeworkRepository.findBySignedHour(teachingTimeslot.getSignedHour());
            if (homework != null) {
                return homework.getDescription();
            }
        }
        return "";
    }

    @Named("getHomeworkDate")
    default LocalDate getHomeworkDate(TeachingTimeslot teachingTimeslot, @Context HomeworkRepository homeworkRepository) {
        if (isSigned(teachingTimeslot)) {
            var homework = homeworkRepository.findBySignedHour(teachingTimeslot.getSignedHour());
            if (homework != null) {
                return homework.getDueDate();
            }
        }
        return null;
    }

}

