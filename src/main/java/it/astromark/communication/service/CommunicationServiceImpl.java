package it.astromark.communication.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.communication.dto.CommunicationRequest;
import it.astromark.communication.dto.CommunicationResponse;
import it.astromark.communication.mapping.CommunicationMapper;
import it.astromark.communication.repository.CommunicationRepository;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunicationServiceImpl implements CommunicationService  {

    private final CommunicationRepository communicationRepository;
    private final SchoolUserService schoolUserService;
    private final CommunicationMapper communicationMapper;
    private final AuthenticationService authenticationService;
    private final StudentRepository studentRepository;

    @Autowired
    public CommunicationServiceImpl(CommunicationRepository communicationRepository, SchoolUserService schoolUserService, CommunicationMapper communicationMapper, AuthenticationService authenticationService, StudentRepository studentRepository) {
        this.communicationRepository = communicationRepository;
        this.schoolUserService = schoolUserService;
        this.communicationMapper = communicationMapper;
        this.authenticationService = authenticationService;
        this.studentRepository = studentRepository;
    }

    @Override
    public CommunicationResponse create(CommunicationRequest communicationRequest) {
        return null;
    }

    @Override
    public CommunicationResponse update(Integer integer, CommunicationRequest communicationRequest) {
        return null;
    }

    @Override
    public CommunicationResponse delete(Integer integer) {
        return null;
    }

    @Override
    public CommunicationResponse getById(Integer integer) {
        return null;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('student') || hasRole('parent') || hasRole('teacher')")
    public List<CommunicationResponse> getCommunicationBySchoolClassId(Integer schoolClassId) {
        if(!schoolUserService.isLoggedTeacherClass(schoolClassId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if (!schoolUserService.isLoggedParentStudentClass(schoolClassId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        } else if(authenticationService.isStudent() && !studentRepository.existsStudentByIdAndSchoolClasses_Id(authenticationService.getStudent().orElseThrow().getId(),schoolClassId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        return communicationMapper.toCommunicationResponseList(communicationRepository.findBySchoolClass_Id(schoolClassId));
    }

}
