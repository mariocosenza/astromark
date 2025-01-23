package it.astromark.classwork.service;

import it.astromark.classwork.dto.ClassworkResponse;
import it.astromark.classwork.dto.HomeworkResponse;

import java.util.List;

public interface ClassworkService {
    List<ClassworkResponse> getClassActivities(Integer classId);

    List<HomeworkResponse> getHomework(Integer classId);
}
