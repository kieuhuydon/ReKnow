package com.huydon.reknow.repository;

import com.huydon.reknow.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long > {
    // khi có keyword
    //theo kiểu tuyệt đối
    //Page<Book> findByUserIdAndTitleIgnoreCaseAndDeletedAtIsNull(Long userId, String keyword, Pageable pageable);

    //theo kiểu like
    Page<Book> findByUserIdAndTitleContainingIgnoreCaseAndDeletedAtIsNull(Long userId, String keyword, Pageable pageable);

    // khi không có keyword
    Page<Book> findByUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);

    Optional<Book> findByIdAndDeletedAtIsNull (Long id);
}
