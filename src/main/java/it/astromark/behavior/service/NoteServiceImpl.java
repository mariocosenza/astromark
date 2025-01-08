package it.astromark.behavior.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.behavior.dto.NoteRequest;
import it.astromark.behavior.dto.NoteResponse;
import it.astromark.behavior.mapper.NoteMapper;
import it.astromark.behavior.repository.NoteRepository;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.service.StudentService;
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
    private final StudentService studentService;
    private final NoteMapper noteMapper;
    private final SchoolUserService schoolUserService;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, StudentService studentService, NoteMapper noteMapper, SchoolUserService schoolUserService) {
        this.noteRepository = noteRepository;
        this.studentService = studentService;
        this.noteMapper = noteMapper;
        this.schoolUserService = schoolUserService;
    }

    @Override
    public NoteResponse create(NoteRequest noteRequest) {
        return null;
    }

    @Override
    public NoteResponse update(UUID uuid, NoteRequest noteRequest) {
        return null;
    }

    @Override
    public NoteResponse delete(UUID uuid) {
        return null;
    }

    @Override
    public NoteResponse getById(UUID uuid) {
        return null;
    }


    @Override
    @Transactional
    @PreAuthorize("hasRole('student') || hasRole('parent') || hasRole('teacher')")
    public List<NoteResponse> getNoteByStudentId(UUID studentId, Integer classId) {
        if (!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if (!schoolUserService.isLoggedTeacherStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        return studentService.getById(studentId).getNotes().stream()
                .filter(m -> m.getStudent().getSchoolClasses().stream().anyMatch(c -> c.getId().equals(classId)))
                .map(noteMapper::toNoteResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('student') || hasRole('parent')")
    public void view(UUID studentId, UUID noteId) {
        if(!schoolUserService.isLoggedUserParent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if(!schoolUserService.isLoggedStudent(studentId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        noteRepository.findById(noteId).ifPresent(note -> {
            if(note.getStudent().getId().equals(studentId)) {
                note.setViewed(true);
                noteRepository.save(note);
            }
        });
    }
}
