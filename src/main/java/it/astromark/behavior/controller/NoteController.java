package it.astromark.behavior.controller;

import it.astromark.behavior.dto.NoteResponse;
import it.astromark.behavior.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/students")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @Operation(
            summary = "Retrieve notes for a student by class ID",
            description = "Gets a list of notes associated with a specific student and class."
    )
    @GetMapping("/{studentId}/notes/{classId}")
    public List<NoteResponse> getNotesByStudentIdAndClassId(@PathVariable UUID studentId, @PathVariable Integer classId) {
        return noteService.getNoteByStudentId(studentId, classId);
    }

    @Operation(
            summary = "Update note as viewed",
            description = "Marks a specific note as viewed for a given student and note ID."
    )
    @PatchMapping("/{studentId}/notes/{noteId}")
    public void updateNoteByStudentId(@PathVariable UUID studentId, @PathVariable UUID noteId) {
        noteService.view(studentId, noteId);
    }
}
