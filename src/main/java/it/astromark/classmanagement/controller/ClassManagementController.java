package it.astromark.classmanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/class-management")
public class ClassManagementController {

    @GetMapping("/year")
    public String getYear() {
        return "2024";
    }


}
