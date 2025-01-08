package it.astromark.behavior.service;


import it.astromark.behavior.dto.NoteRequest;
import it.astromark.behavior.dto.NoteResponse;
import it.astromark.behavior.entity.Note;
import it.astromark.commons.service.CrudService;
import it.astromark.user.student.entity.Student;

import java.util.List;
import java.util.UUID;

public interface NoteService extends CrudService<Note, NoteRequest, NoteResponse, UUID> {
    List<NoteResponse> getNoteByStudentId(UUID studentId, Integer classId);
    void view(UUID studentId, UUID noteId);
}
