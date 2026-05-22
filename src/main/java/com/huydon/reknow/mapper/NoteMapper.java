package com.huydon.reknow.mapper;

import com.huydon.reknow.dto.note.NoteRequest;
import com.huydon.reknow.dto.note.NoteResponse;
import com.huydon.reknow.entity.Note;
import org.mapstruct.*;

@Mapper(componentModel= "spring")
public interface NoteMapper {
     Note toEntity(NoteRequest req);
     @Mapping(source = "book.id", target = "bookId")
     //đi sâu vào book trong note, lấy book id, gán vào bookId trong DTO
    NoteResponse toResponse(Note note);

     @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
     void updateNote (NoteRequest req, @MappingTarget Note note);
}
