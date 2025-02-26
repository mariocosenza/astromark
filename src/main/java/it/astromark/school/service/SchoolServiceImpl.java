package it.astromark.school.service;

import it.astromark.school.dto.SchoolRequest;
import it.astromark.school.dto.SchoolResponse;
import it.astromark.school.entity.School;
import it.astromark.school.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    public SchoolServiceImpl(SchoolRepository schoolRepository) {
    }


    @Override
    @PreAuthorize("hasRole('MANAGER')")
    public SchoolResponse create(SchoolRequest schoolRequest) {
        return null;
    }

    @Override
    public SchoolResponse update(String s, SchoolRequest schoolRequest) {
        return null;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public School getById(String s) {
        return null;
    }


}
