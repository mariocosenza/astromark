package it.astromark.classwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.astromark.classwork.dto.ClassworkResponse;
import it.astromark.classwork.dto.HomeworkResponse;
import it.astromark.classwork.service.ClassworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/classwork")
public class ClassworkController {

    private final ClassworkService classworkService;

    @Autowired
    public ClassworkController(ClassworkService classworkService) {
        this.classworkService = classworkService;
    }

    @Operation(
            summary = "Retrieve all class activities",
            description = "Gets a list of all activities associated with a specific class by its ID."
    )
    @GetMapping("/{classId}/activities/all")
    public List<ClassworkResponse> getClassActivities(@PathVariable Integer classId) {
        return classworkService.getClassActivities(classId);
    }

    @Operation(
            summary = "Retrieve all homework assignments",
            description = "Gets a list of all homework assignments for a specific class by its ID."
    )
    @GetMapping("/{classId}/homeworks/all")
    public List<HomeworkResponse> getHomework(@PathVariable Integer classId) {
        return classworkService.getHomework(classId);
    }

    @Operation(
            summary = "Retrieve homework ID for a signed hour",
            description = "Gets the ID of the homework associated with a specific signed hour for a class."
    )
    @GetMapping("/{classId}/homeworks/{signedHourId}")
    public Integer getSignedHourHomeworkId(@PathVariable Integer classId, @PathVariable Integer signedHourId) {
        return classworkService.getSignedHourHomeworkId(classId, signedHourId);
    }
}
