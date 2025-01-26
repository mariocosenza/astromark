package it.astromark.behavior.service;


import it.astromark.authentication.service.AuthenticationService;
import it.astromark.behavior.dto.NoteRequest;
import it.astromark.behavior.dto.NoteResponse;
import it.astromark.behavior.entity.Note;
import it.astromark.behavior.mapper.NoteMapper;
import it.astromark.behavior.repository.NoteRepository;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final SchoolUserService schoolUserService;
    private final StudentRepository studentRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper, SchoolUserService schoolUserService, StudentRepository studentRepository, AuthenticationService authenticationService) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
        this.schoolUserService = schoolUserService;
        this.studentRepository = studentRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public NoteResponse create(NoteRequest noteRequest) {
        if (!schoolUserService.isLoggedTeacherStudent(noteRequest.studentId())) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        var note = new Note();
        note.setStudent(studentRepository.findById(noteRequest.studentId()).orElseThrow());
        note.setDescription(noteRequest.description());
        note.setDate(noteRequest.date());
        note.setViewed(false);

        noteRepository.save(note);

        return noteMapper.toNoteResponse(note);
    }

    @Override
    public NoteResponse update(UUID uuid, NoteRequest noteRequest) {
        return null;
    }

    @Override
    public boolean delete(UUID uuid) {
        return false;
    }

    @Override
    public Note getById(UUID uuid) {
        return null;
    }


    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT') || hasRole('PARENT') || hasRole('TEACHER')")
    public List<NoteResponse> getNoteByStudentId(UUID studentId, Integer classId) {
        if (!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if (authenticationService.isTeacher() && !schoolUserService.isLoggedTeacherStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if (authenticationService.isStudent() && !schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        return studentRepository.findById(studentId).orElseThrow().getNotes().stream()
                .filter(m -> m.getStudent().getSchoolClasses().stream().anyMatch(c -> c.getId().equals(classId)))
                .map(noteMapper::toNoteResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('STUDENT') || hasRole('PARENT')")
    public void view(UUID studentId, UUID noteId) {
        if (!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if (!schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        noteRepository.findById(noteId).ifPresent(note -> {
            if (note.getStudent().getId().equals(studentId)) {
                note.setViewed(true);
                noteRepository.save(note);
            }
        });
    }
}
