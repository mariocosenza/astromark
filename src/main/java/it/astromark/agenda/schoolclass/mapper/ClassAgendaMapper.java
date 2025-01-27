package it.astromark.agenda.schoolclass.mapper;

import it.astromark.agenda.schoolclass.dto.TeachingTimeslotDetailedResponse;
import it.astromark.agenda.schoolclass.entity.TeachingTimeslot;
import it.astromark.classwork.dto.ClassActivityResponse;
import it.astromark.classwork.dto.HomeworkBriefResponse;
import it.astromark.classwork.mapper.ClassworkMapper;
import it.astromark.classwork.repository.ClassActivityRepository;
import it.astromark.classwork.repository.HomeworkRepository;
import it.astromark.user.commons.dto.SchoolUserResponse;
import it.astromark.user.commons.mapper.SchoolUserMapper;
import it.astromark.user.teacher.entity.Teacher;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ClassAgendaMapper {

    @Autowired
    private ClassActivityRepository classActivityRepository;

    @Autowired
    private HomeworkRepository homeworkRepository;

    @Autowired
    private ClassworkMapper classworkMapper;

    @Autowired
    private SchoolUserMapper schoolUserMapper;

    @Mapping(target = "subject", source = "teaching.subjectTitle.title")
    @Mapping(target = "signed", expression = "java(teachingTimeslot.getSignedHour() != null)")
    @Mapping(target = "teacher", source = "teaching.teacher", qualifiedByName = "mapTeacher")
    @Mapping(target = "activity", source = "teachingTimeslot", qualifiedByName = "mapActivity")
    @Mapping(target = "homework", source = "teachingTimeslot", qualifiedByName = "mapHomework")
    public abstract TeachingTimeslotDetailedResponse toTeachingTimeslotDetailedResponse(TeachingTimeslot teachingTimeslot);

    public abstract List<TeachingTimeslotDetailedResponse> toTeachingTimeslotDetailedResponseList(List<TeachingTimeslot> teachingTimeslots);

    @Named("mapTeacher")
    SchoolUserResponse mapTeacher(Teacher teacher) {
        return schoolUserMapper.toSchoolUserResponse(teacher);
    }

    @Named("mapActivity")
    ClassActivityResponse mapActivity(TeachingTimeslot teachingTimeslot) {
        if (teachingTimeslot.getSignedHour() != null) {
            var activity = classActivityRepository.findBySignedHour(teachingTimeslot.getSignedHour());
            if (activity != null) {
                return classworkMapper.toClassActivityResponse(activity);
            }
        }
        return null;
    }

    @Named("mapHomework")
    HomeworkBriefResponse mapHomework(TeachingTimeslot teachingTimeslot) {
        if (teachingTimeslot.getSignedHour() != null) {
            var homework = homeworkRepository.findBySignedHour(teachingTimeslot.getSignedHour());
            if (homework != null) {
                return classworkMapper.toHomeworkBriefResponse(homework);
            }
        }
        return null;
    }
}
