package it.astromark.communication.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.astromark.communication.dto.CommunicationRequest;
import it.astromark.communication.dto.CommunicationResponse;
import it.astromark.communication.service.CommunicationService;
import org.springframework.web.bind.annotation.*;

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

    @Operation(
            summary = "Create a new communication",
            description = "Creates a new communication for a specific school class."
    )
    @PostMapping("/communication")
    public CommunicationResponse create(@RequestBody CommunicationRequest communicationRequest) {
        return communicationService.create(communicationRequest);
    }

    @Operation(
            summary = "Update an existing communication",
            description = "Updates a specific communication by its ID."
    )
    @PatchMapping("/communication/{communicationId}")
    public CommunicationResponse update(@RequestBody CommunicationRequest communicationRequest, @PathVariable Integer communicationId) {
        return communicationService.update(communicationId, communicationRequest);
    }

}
