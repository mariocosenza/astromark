package it.astromark.behavior.controller;

import it.astromark.behavior.dto.NoteResponse;
import it.astromark.behavior.service.NoteService;
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

    @GetMapping("/{studentId}/notes/{classId}")
    public List<NoteResponse> getNotesByStudentIdAndClassId(@PathVariable UUID studentId, @PathVariable Integer classId) {
        return noteService.getNoteByStudentId(studentId, classId);
    }

    @PatchMapping("/{studentId}/notes/{noteId}")
    public void updateNoteByStudentId(@PathVariable UUID studentId, @PathVariable UUID noteId) {
        noteService.view(studentId, noteId);
    }

}
