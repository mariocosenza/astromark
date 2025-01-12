package it.astromark.agenda.schoolclass.service;

import it.astromark.agenda.schoolclass.dto.SignHourResponse;
import it.astromark.agenda.schoolclass.entity.SignedHour;
import it.astromark.agenda.schoolclass.entity.TeachingTimeslot;
import it.astromark.agenda.schoolclass.mapper.SignedHourMapper;
import it.astromark.agenda.schoolclass.repository.TeachingTimeslotRepository;
import it.astromark.classwork.repository.ClassActivityRepository;
import it.astromark.classwork.repository.HomeworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class SignedHourServiceImpl implements SignedHourService {

    private final TeachingTimeslotRepository teachingTimeslotRepository;
    private final SignedHourMapper signedHourMapper;
    private final ClassActivityRepository classActivityRepository;
    private final HomeworkRepository homeworkRepository;

    @Autowired
    public SignedHourServiceImpl(TeachingTimeslotRepository teachingTimeslotRepository, SignedHourMapper signedHourMapper, ClassActivityRepository classActivityRepository, HomeworkRepository homeworkRepository) {
        this.teachingTimeslotRepository = teachingTimeslotRepository;
        this.signedHourMapper = signedHourMapper;
        this.classActivityRepository = classActivityRepository;
        this.homeworkRepository = homeworkRepository;
    }

    @Override
    @Transactional
    public List<SignHourResponse> getSignedHours(Integer classId, LocalDate localDate) {
        List<TeachingTimeslot> teachingTimeslotList = teachingTimeslotRepository.findByClassTimetableSchoolClass_IdAndDate(classId, localDate);
        return signedHourMapper.toSignedHourResponseList(teachingTimeslotList, classActivityRepository, homeworkRepository);
    }

    @Override
    public SignedHour create(SignedHour signedHour) {
        return null;
    }

    @Override
    public SignedHour update(Integer integer, SignedHour signedHour) {
        return null;
    }

    @Override
    public SignedHour delete(Integer integer) {
        return null;
    }

    @Override
    public SignedHour getById(Integer integer) {
        return null;
    }
}
