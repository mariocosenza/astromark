package it.astromark.behavior.mapper;

import it.astromark.behavior.dto.NoteResponse;
import it.astromark.behavior.entity.Note;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface NoteMapper {
    NoteResponse toNoteResponse(Note note);
}
