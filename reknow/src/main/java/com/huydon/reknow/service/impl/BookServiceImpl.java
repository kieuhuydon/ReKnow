package com.huydon.reknow.service.impl;

import com.huydon.reknow.common.response.PageResponse;
import com.huydon.reknow.dto.book.BookRequest;
import com.huydon.reknow.dto.book.BookResponse;
import com.huydon.reknow.entity.Book;
import com.huydon.reknow.entity.Note;
import com.huydon.reknow.entity.User;
import com.huydon.reknow.exception.AppException;
import com.huydon.reknow.exception.ResourceNotFoundException;
import com.huydon.reknow.mapper.BookMapper;
import com.huydon.reknow.repository.BookRepository;
import com.huydon.reknow.repository.NoteRepository;
import com.huydon.reknow.repository.UserRepository;
import com.huydon.reknow.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;
    private final NoteRepository noteRepository;

    @Override
    public BookResponse createBook(BookRequest req) {
        //lấy email từ token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        //tìm user
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found"));
        //map bookreq -> book entity
        Book book = bookMapper.toEntity(req);
        //set user vào book
        book.setUser(user);
        Book savedBook = bookRepository.save(book);
        BookResponse bookRes = bookMapper.toResponse(savedBook);

        return bookRes;
    }

    @Override
    public PageResponse<BookResponse> getBooks(String keyword, int page, int size, String sortBy, String direction) {
        //lấy email từ token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        //tìm user
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found"));

        // tạo sort
        Sort sort = direction.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // tạo pageable
        int pageIndex = page<1 ? 0 : page-1;
        Pageable pageable = PageRequest.of(pageIndex, size, sort);

        //có keyword -> search
        //không có keyword -> lấy tất cả
        Page<Book> bookPage = (keyword != null && !keyword.isBlank())
                ? bookRepository.findByUserIdAndTitleContainingIgnoreCaseAndDeletedAtIsNull(user.getId(), keyword, pageable)
                : bookRepository.findByUserIdAndDeletedAtIsNull(user.getId(), pageable);

        // map entity sang response DTO
        Page<BookResponse> bookResponsePage = bookPage.map(book -> bookMapper.toResponse(book));

        List <BookResponse> books = bookResponsePage.getContent();

        return PageResponse.<BookResponse>builder()
                .data(books)
                .currentPage(bookResponsePage.getNumber())
                .pageSize(bookResponsePage.getSize())
                .totalElements(bookResponsePage.getTotalElements())
                .totalPages(bookResponsePage.getTotalPages())
                .build();
    }



    @Override
    public BookResponse updateBook(Long id, BookRequest req) {
        // tìm book
        Book currentBook = bookRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("Book not found"));
        // quyển sở hữu
        validateOwnerShip(currentBook);
        // update file
        bookMapper.updateBook(req, currentBook);

        bookRepository.save(currentBook);

        return bookMapper.toResponse(currentBook);

    }


    @Override
    @Transactional  // xóa book phải xóa luôn tất note
    public void deleteBook(Long id) {
        // tìm book
        Book currentBook = bookRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("Book not found"));
        // quyển sở hữu
        validateOwnerShip(currentBook);
        // soft delte book set deletedAt = now
        LocalDateTime now = LocalDateTime.now();
        currentBook.setDeletedAt(now);
        bookRepository.save(currentBook);
        // soft delte tất cả note thuộc book
        List<Note> notes = noteRepository.findAllByBookAndDeletedAtIsNull(currentBook);
        for(Note note: notes){
            note.setDeletedAt(now);
        }
        noteRepository.saveAll(notes);

    }

    @Override
    public BookResponse getBookById(Long id) {
        // lấy book theo id
        Book book = bookRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("Book not found"));
        // kiểm tra quyến sở hữu
        validateOwnerShip(book);

        return bookMapper.toResponse(book);
    }

    private void validateOwnerShip (Book book){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!book.getUser().getEmail().equals(email)){
            throw new AppException( 403, "Forbidden");
        }
    }


}
