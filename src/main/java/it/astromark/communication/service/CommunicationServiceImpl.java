package it.astromark.communication.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.classmanagement.repository.SchoolClassRepository;
import it.astromark.commons.exception.GlobalExceptionHandler;
import it.astromark.communication.dto.CommunicationRequest;
import it.astromark.communication.dto.CommunicationResponse;
import it.astromark.communication.entity.Communication;
import it.astromark.communication.mapping.CommunicationMapper;
import it.astromark.communication.repository.CommunicationRepository;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class CommunicationServiceImpl implements CommunicationService {

    private final CommunicationRepository communicationRepository;
    private final SchoolUserService schoolUserService;
    private final CommunicationMapper communicationMapper;
    private final AuthenticationService authenticationService;
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;

    @Autowired
    public CommunicationServiceImpl(CommunicationRepository communicationRepository, SchoolUserService schoolUserService, CommunicationMapper communicationMapper, AuthenticationService authenticationService, StudentRepository studentRepository, SchoolClassRepository schoolClassRepository) {
        this.communicationRepository = communicationRepository;
        this.schoolUserService = schoolUserService;
        this.communicationMapper = communicationMapper;
        this.authenticationService = authenticationService;
        this.studentRepository = studentRepository;
        this.schoolClassRepository = schoolClassRepository;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public CommunicationResponse create(CommunicationRequest communicationRequest) {
        if (!schoolUserService.isLoggedTeacherClass(communicationRequest.classId())) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        }

        var communication = new Communication();
        communication.setTitle(communicationRequest.title());
        communication.setDescription(communicationRequest.description());
        communication.setDate(LocalDate.now());
        communication.setSchoolClass(schoolClassRepository.findById(communicationRequest.classId()).orElse(null));

        communicationRepository.save(communication);

        return communicationMapper.toCommunicationResponse(communication);
    }


    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public CommunicationResponse update(Integer integer, CommunicationRequest communicationRequest) {
        if (!schoolUserService.isLoggedTeacherClass(communicationRequest.classId())) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        }

        var communication = communicationRepository.findById(integer).orElseThrow();
        communication.setTitle(communicationRequest.title());
        communication.setDescription(communicationRequest.description());

        communicationRepository.save(communication);

        return communicationMapper.toCommunicationResponse(communication);
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }

    @Override
    public Communication getById(Integer integer) {
        return null;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT') || hasRole('PARENT') || hasRole('TEACHER')")
    public List<CommunicationResponse> getCommunicationBySchoolClassId(Integer schoolClassId) {
        if (!schoolUserService.isLoggedTeacherClass(schoolClassId)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        } else if (!schoolUserService.isLoggedParentStudentClass(schoolClassId)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        } else if (authenticationService.isStudent() && !studentRepository.existsStudentByIdAndSchoolClasses_Id(authenticationService.getStudent().orElseThrow().getId(), schoolClassId)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        }

        var communications = communicationRepository.findBySchoolClass_Id(schoolClassId)
                .stream()
                .sorted(Comparator.comparing(Communication::getDate))
                .toList();

        return communicationMapper.toCommunicationResponseList(communications);
    }

}
