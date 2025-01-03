package it.astromark.user.commons.model;

import it.astromark.user.parent.entity.Parent;
import it.astromark.user.secretary.entity.Secretary;
import it.astromark.user.student.entity.Student;
import it.astromark.user.teacher.entity.Teacher;

public enum Role {
    SECRETARY,
    TEACHER,
    STUDENT,
    PARENT,
    ;

    public static String getRole(SchoolUser user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        return switch (user) {
            case Parent ignored -> "ROLE_" + Role.PARENT.toString().toLowerCase();
            case Teacher ignored -> "ROLE_" + Role.TEACHER.toString().toLowerCase();
            case Student ignored -> "ROLE_" + Role.STUDENT.toString().toLowerCase();
            case Secretary ignored -> "ROLE_" + Role.SECRETARY.toString().toLowerCase();
            default -> throw new IllegalStateException("Unexpected user type: " + user.getClass());
        };
    }


}

