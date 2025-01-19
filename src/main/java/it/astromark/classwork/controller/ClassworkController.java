package it.astromark.classwork.controller;

import it.astromark.classwork.dto.ClassworkResponse;
import it.astromark.classwork.dto.HomeworkResponse;
import it.astromark.classwork.service.ClassworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/classwork")
public class ClassworkController {

    private final ClassworkService classworkService;

    @Autowired
    public ClassworkController(ClassworkService classworkService) {
        this.classworkService = classworkService;
    }

    @GetMapping("/{classId}/activities/all")
    public List<ClassworkResponse> getClassActivities(@PathVariable Integer classId) {
        return classworkService.getClassActivities(classId);
    }

    @GetMapping("/{classId}/homeworks/all")
    public List<HomeworkResponse> getHomework(@PathVariable Integer classId) {
        return classworkService.getHomework(classId);
    }

}
