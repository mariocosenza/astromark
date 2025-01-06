package it.astromark.user.commons.service;

import it.astromark.user.parent.entity.Parent;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SchoolUserServiceImpl implements SchoolUserService {


    @Override
    public boolean isStudentParent(Parent parent, UUID studentId) {
        return parent.getStudents().stream().anyMatch(s -> s.getId().equals(studentId));
    }
}
