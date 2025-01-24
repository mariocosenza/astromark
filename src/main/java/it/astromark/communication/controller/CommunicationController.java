package it.astromark.communication.controller;

import it.astromark.communication.dto.CommunicationResponse;
import it.astromark.communication.service.CommunicationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/schoolClasses")
public class CommunicationController {

    private final CommunicationService communicationService;

    public CommunicationController(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @Operation(
            summary = "Retrieve communications by school class ID",
            description = "Gets a list of communications associated with a specific school class by its ID."
    )
    @GetMapping("/{schoolClassId}/communications")
    public List<CommunicationResponse> getCommunicationBySchoolClassId(@PathVariable Integer schoolClassId) {
        return communicationService.getCommunicationBySchoolClassId(schoolClassId);
    }

}
