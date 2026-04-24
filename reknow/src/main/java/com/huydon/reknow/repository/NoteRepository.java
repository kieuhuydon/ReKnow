package com.huydon.reknow.repository;

import com.huydon.reknow.entity.Book;
import com.huydon.reknow.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    //Trash module
    // nhìn vào đối tượng Book gắn với Note, chỉ lấy Book nào có userId khơps với tham số truyền vào
    Page<Note> findByBook_UserIdAndDeletedAtIsNotNull (Long userId, Pageable pageable);

    List<Note> findAllByBook(Book book);

    Optional<Note> findByIdAndDeletedAtIsNotNull(Long id);

    //hard delete sau 15 ngày
    @Query(""" 
    Select n From Note n 
    Where n.deletedAt Is Not Null 
    And n.deletedAt <= :cutoffDate
    """)
    List<Note> findNotesToDelete (@Param( "cutoffDate" ) LocalDateTime cutoffDate);

}
