package it.astromark.user.teacher.dto;

import java.util.List;

public record TeacherDetailsResponse(String username , List<String> teaching ) {
}
