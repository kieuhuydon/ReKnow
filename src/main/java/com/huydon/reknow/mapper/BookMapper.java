package com.huydon.reknow.mapper;

import com.huydon.reknow.dto.book.BookRequest;
import com.huydon.reknow.dto.book.BookResponse;
import com.huydon.reknow.entity.Book;
import org.mapstruct.*;

@Mapper(componentModel= "spring")
public interface  BookMapper {
    Book toEntity (BookRequest req);


    BookResponse toResponse(Book book);

    // MapStruct tự update field không null
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBook (BookRequest req, @MappingTarget Book book);


}
