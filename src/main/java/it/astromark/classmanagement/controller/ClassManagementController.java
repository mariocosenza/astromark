package it.astromark.classmanagement.controller;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.classmanagement.service.ClassManagementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("api/class-management")
public class ClassManagementController {

    private final ClassManagementService classManagementService;

    public ClassManagementController(ClassManagementService classManagementService) {
        this.classManagementService = classManagementService;
    }

    @GetMapping("/year")
    public Year getYear() {
        return classManagementService.getYear();
    }

    @GetMapping("/all")
    List<SchoolClassResponse> getClasses() {
        return classManagementService.getClasses();
    }

}
