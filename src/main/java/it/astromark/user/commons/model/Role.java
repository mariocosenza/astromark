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
            case Parent ignored -> "ROLE_" + Role.PARENT.toString().toUpperCase();
            case Teacher ignored -> "ROLE_" + Role.TEACHER.toString().toUpperCase();
            case Student ignored -> "ROLE_" + Role.STUDENT.toString().toUpperCase();
            case Secretary ignored -> "ROLE_" + Role.SECRETARY.toString().toUpperCase();
            default -> throw new IllegalStateException("Unexpected user type: " + user.getClass());
        };
    }


}

