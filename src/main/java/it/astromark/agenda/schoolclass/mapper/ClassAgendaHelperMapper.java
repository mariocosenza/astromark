package it.astromark.agenda.schoolclass.mapper;

import it.astromark.agenda.schoolclass.entity.TeachingTimeslot;
import it.astromark.classwork.dto.ClassActivityResponse;
import it.astromark.classwork.dto.HomeworkBriefResponse;
import it.astromark.classwork.mapper.ClassworkMapper;
import it.astromark.classwork.repository.ClassActivityRepository;
import it.astromark.classwork.repository.HomeworkRepository;
import it.astromark.user.commons.dto.SchoolUserResponse;
import it.astromark.user.commons.mapper.SchoolUserMapper;
import it.astromark.user.teacher.entity.Teacher;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class ClassAgendaHelperMapper {

    private final SchoolUserMapper schoolUserMapper;
    private final ClassActivityRepository classActivityRepository;
    private final ClassworkMapper classworkMapper;
    private final HomeworkRepository homeworkRepository;

    public ClassAgendaHelperMapper(SchoolUserMapper schoolUserMapper, ClassActivityRepository classActivityRepository, ClassworkMapper classworkMapper, HomeworkRepository homeworkRepository) {
        this.schoolUserMapper = schoolUserMapper;
        this.classActivityRepository = classActivityRepository;
        this.classworkMapper = classworkMapper;
        this.homeworkRepository = homeworkRepository;
    }

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
