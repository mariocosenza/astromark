package it.astromark.user.commons.service;

import it.astromark.user.parent.entity.Parent;

import java.util.UUID;

public interface SchoolUserService {
    boolean isStudentParent(Parent parent, UUID studentId);
    boolean isLoggedUserParent(UUID studentId);
}
