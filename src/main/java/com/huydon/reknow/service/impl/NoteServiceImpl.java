package com.huydon.reknow.service.impl;

import com.huydon.reknow.common.response.PageResponse;
import com.huydon.reknow.dto.note.NoteRequest;
import com.huydon.reknow.dto.note.NoteResponse;
import com.huydon.reknow.entity.Book;
import com.huydon.reknow.entity.Note;
import com.huydon.reknow.exception.AppException;
import com.huydon.reknow.exception.ResourceNotFoundException;
import com.huydon.reknow.mapper.NoteMapper;
import com.huydon.reknow.repository.BookRepository;
import com.huydon.reknow.repository.NoteRepository;
import com.huydon.reknow.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final BookRepository bookRepository;
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;


    @Override
    public NoteResponse createNote(Long bookId, NoteRequest req) {
        // tìm book theo bookId -> 404
        Book book = bookRepository.findByIdAndDeletedAtIsNull(bookId)
                .orElseThrow(()-> new ResourceNotFoundException("Book is not found"));
        // book thuộc user hiên tại không -> 403
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!book.getUser().getEmail().equals(email)){
            throw new AppException(403, "Forbidden");
        }

        Note note = noteMapper.toEntity(req);
        note.setBook(book);

        Note noteSaved = noteRepository.save(note);

        return noteMapper.toResponse(noteSaved);


    }

    @Override
    public NoteResponse updateNote(Long id, NoteRequest req) {
        Note currentNote = noteRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("Note is not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!currentNote.getBook().getUser().getEmail().equals(email)){
            throw new AppException(403, "Forbidden");
        }

        noteMapper.updateNote(req, currentNote);

        noteRepository.save(currentNote);
        return noteMapper.toResponse(currentNote);



    }

    @Override
    public void deleteNote(Long id) {
        Note currentNote = noteRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("Note is not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!currentNote.getBook().getUser().getEmail().equals(email)){
            throw new AppException(403, "Forbidden");
        }

        currentNote.setDeletedAt(LocalDateTime.now());

        noteRepository.save(currentNote);

    }

    @Override
    public NoteResponse getNoteById(Long id) {
        Note currentNote = noteRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("Note is not found"));


        return noteMapper.toResponse(currentNote);
    }

    @Override
    public PageResponse<NoteResponse> getNotes(Long bookId, String keyword, int page, int size) {
        // tìm book theo bookId -> 404
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new ResourceNotFoundException("Book is not found"));
        // book thuộc user hiên tại không -> 403
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!book.getUser().getEmail().equals(email)){
            throw new AppException(403, "Forbidden");
        }
        // tạo pageable
        int pageIndex = (page>0) ? page-1 : page;
        Pageable pageable = PageRequest.of(pageIndex, size);


        //query Page<Note>
        Page<Note> notePage= (keyword != null && !keyword.isBlank())
                ? noteRepository.findByBookIdAndContentContainingIgnoreCaseAndDeletedAtIsNull(bookId, keyword, pageable)
                : noteRepository.findByBookIdAndDeletedAtIsNull(bookId, pageable);

        // conver Page<NoteRepository>
        Page<NoteResponse> noteResponsePage = notePage.map(note -> noteMapper.toResponse(note));
        List<NoteResponse> notes = noteResponsePage.getContent();

        return PageResponse.<NoteResponse>builder()
                .data(notes)
                .currentPage(noteResponsePage.getNumber())
                .pageSize(noteResponsePage.getSize())
                .totalElements(noteResponsePage.getTotalElements())
                .totalPages(noteResponsePage.getTotalPages())
                .build();
    }

    public void validateBook(Long bookId){
        // tìm book theo bookId -> 404
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new ResourceNotFoundException("Book is not found"));
        // book thuộc user hiên tại không -> 403
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!book.getUser().getEmail().equals(email)){
            throw new AppException(403, "Forbidden");
        }
    }


}
