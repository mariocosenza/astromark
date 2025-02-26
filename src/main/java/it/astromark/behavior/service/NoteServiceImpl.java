package it.astromark.behavior.service;


import it.astromark.behavior.dto.NoteRequest;
import it.astromark.behavior.dto.NoteResponse;
import it.astromark.behavior.entity.Note;
import it.astromark.behavior.mapper.NoteMapper;
import it.astromark.behavior.repository.NoteRepository;
import it.astromark.commons.exception.GlobalExceptionHandler;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final SchoolUserService schoolUserService;
    private final StudentRepository studentRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper, SchoolUserService schoolUserService, StudentRepository studentRepository) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
        this.schoolUserService = schoolUserService;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public NoteResponse create(@NotNull NoteRequest noteRequest) {
        if (!schoolUserService.isLoggedTeacherStudent(noteRequest.studentId()) && noteRequest.date().isBefore(LocalDate.now())) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
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
    public List<NoteResponse> getNoteByStudentId(@NotNull UUID studentId, @NotNull Integer classId) {
        if (!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        } else if (!schoolUserService.isLoggedTeacherStudent(studentId)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        } else if (!schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        }
        return studentRepository.findById(studentId).orElseThrow().getNotes().stream()
                .filter(m -> m.getStudent().getSchoolClasses().stream().anyMatch(c -> c.getId().equals(classId)))
                .map(noteMapper::toNoteResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('STUDENT') || hasRole('PARENT')")
    public void view(@NotNull UUID studentId, @NotNull UUID noteId) {
        if (!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        } else if (!schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        }
        noteRepository.findById(noteId).ifPresent(note -> {
            if (note.getStudent().getId().equals(studentId)) {
                note.setViewed(true);
                noteRepository.save(note);
            }
        });
    }
}
