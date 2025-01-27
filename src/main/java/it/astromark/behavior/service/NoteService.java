package it.astromark.behavior.service;


import it.astromark.behavior.dto.NoteRequest;
import it.astromark.behavior.dto.NoteResponse;
import it.astromark.behavior.entity.Note;
import it.astromark.commons.service.CrudService;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing notes related to student behavior.
 * Extends the generic CrudService interface to provide CRUD functionality for notes.
 */
public interface NoteService extends CrudService<Note, NoteRequest, NoteResponse, UUID> {
    /**
     * Retrieves a list of notes for a specific student in a given class.
     *
     * @param studentId the UUID of the student
     * @param classId   the ID of the class
     * @return a list of `NoteResponse` objects representing the notes
     * Pre-condition: The `studentId` and `classId` must not be null. The student and class associated with the provided IDs must exist.
     * Post-condition: Returns a list of notes associated with the specified student and class.
     */
    List<NoteResponse> getNoteByStudentId(UUID studentId, Integer classId);

    /**
     * Marks a specific note as viewed for a student.
     *
     * @param studentId the UUID of the student
     * @param noteId    the UUID of the note
     *                  Pre-condition: The `studentId` and `noteId` must not be null. The note associated with the `noteId` must exist and belong to the specified student.
     *                  Post-condition: The note is marked as viewed for the specified student.
     */
    void view(UUID studentId, UUID noteId);

}
