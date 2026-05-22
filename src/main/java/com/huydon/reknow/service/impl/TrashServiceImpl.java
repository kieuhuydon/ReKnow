package com.huydon.reknow.service.impl;

import com.huydon.reknow.common.response.PageResponse;
import com.huydon.reknow.dto.book.BookResponse;
import com.huydon.reknow.dto.note.NoteResponse;
import com.huydon.reknow.entity.Book;
import com.huydon.reknow.entity.Note;
import com.huydon.reknow.entity.User;
import com.huydon.reknow.exception.AppException;
import com.huydon.reknow.exception.ResourceNotFoundException;
import com.huydon.reknow.mapper.BookMapper;
import com.huydon.reknow.mapper.NoteMapper;
import com.huydon.reknow.repository.BookRepository;
import com.huydon.reknow.repository.NoteRepository;
import com.huydon.reknow.repository.UserRepository;
import com.huydon.reknow.service.TrashService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrashServiceImpl implements TrashService {
    private final BookRepository bookRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;
    private final NoteMapper noteMapper;

    @Override
    public PageResponse<BookResponse> getDeletedBooks(int page, int size) {
        int pageIndex = (page>0) ? page - 1 : page;
        Pageable pageable = PageRequest.of(pageIndex, size);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User is not found"));
        Page<Book> bookPage = bookRepository.findByUserIdAndDeletedAtIsNotNull(currentUser.getId(), pageable);

        Page<BookResponse> bookResponsePage = bookPage.map(book -> bookMapper.toResponse(book));


        return PageResponse.<BookResponse>builder()
                .data(bookResponsePage.getContent())
                .currentPage(bookResponsePage.getNumber())
                .pageSize(bookResponsePage.getSize())
                .totalElements(bookResponsePage.getTotalElements())
                .totalPages(bookResponsePage.getTotalPages())
                .build();
    }

    @Override
    public PageResponse<NoteResponse> getDeletedNotes(int page, int size) {
        int pageIndex = (page>0) ? page - 1 : page;
        Pageable pageable = PageRequest.of(pageIndex, size);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User is not found"));
        Page<Note> notePage = noteRepository.findByBook_UserIdAndDeletedAtIsNotNull(currentUser.getId(), pageable);

        Page<NoteResponse> noteResponsePage = notePage.map(note -> noteMapper.toResponse(note));


        return PageResponse.<NoteResponse>builder()
                .data(noteResponsePage.getContent())
                .currentPage(noteResponsePage.getNumber())
                .pageSize(noteResponsePage.getSize())
                .totalElements(noteResponsePage.getTotalElements())
                .totalPages(noteResponsePage.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public void restoreBook(Long id) {
        // tim book
        Book book = bookRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("Book is not found"));;

        // kiểm tra ownership
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found"));

        if(!book.getUser().getEmail().equals(email)){
            throw new AppException( 403, "Forbidden");
        }
        book.setDeletedAt(null);
        bookRepository.save(book);
        //restore tất cả note thuộc book
        List<Note> notes = noteRepository.findAllByBook(book);
        for(Note note:notes){
            note.setDeletedAt(null);
        }
        noteRepository.saveAll(notes);



    }

    @Override
    @Transactional
    public void restoreNote(Long id) {
        Note note = noteRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("Note is note found"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!note.getBook().getUser().getEmail().equals(email)){
            throw new AppException(403, "Forbidden");
        }
        note.setDeletedAt(null);
        noteRepository.save(note);

    }

    @Override
    @Transactional
    public void restoreAll() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User is not found"));
        List<Book> books = bookRepository.findAllByUserIdAndDeletedAtIsNotNull(currentUser.getId());

        for(Book book: books){
            restoreBook(book.getId());
        }
    }

    @Override
    public void permanentDeleteNote(Long id) {
        Note note = noteRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("Note is note found"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!note.getBook().getUser().getEmail().equals(email)){
            throw new AppException(403, "Forbidden");
        }

        noteRepository.delete(note);

    }

    @Override
    @Transactional
    public void permanentDeleteBook(Long id) {
        // tim book
        Book book = bookRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("Book is not found"));;

        // kiểm tra ownership
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found"));

        if(!book.getUser().getEmail().equals(email)){
            throw new AppException( 403, "Forbidden");
        }
        bookRepository.delete(book);
        //restore tất cả note thuộc book
        List<Note> notes = noteRepository.findAllByBook(book);
        for(Note note:notes){
            noteRepository.delete(note);
        }



    }
}
