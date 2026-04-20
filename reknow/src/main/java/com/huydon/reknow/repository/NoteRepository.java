package com.huydon.reknow.repository;

import com.huydon.reknow.entity.Book;
import com.huydon.reknow.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    //lấy danh sách note cuả book
    List<Note> findAllByBookAndDeletedAtIsNull(Book book);

    Optional<Note> findByIdAndDeletedAtIsNull(Long id);

    //lấy note của book, có phân trang
    Page<Note> findByBookIdAndDeletedAtIsNull(Long bookId, Pageable pageAble);

    //search note theo content, có phân trang
    Page<Note> findByBookIdAndContentContainingIgnoreCaseAndDeletedAtIsNull(Long bookId, String content, Pageable pageAble);
}
