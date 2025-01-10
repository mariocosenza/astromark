package it.astromark.classwork.service;

import it.astromark.classwork.dto.ClassworkResponse;

import java.util.List;

public interface ClassworkService {
    List<ClassworkResponse> getClassActivities(Integer classId);
}
