package com.huydon.reknow.service;

import com.huydon.reknow.common.response.PageResponse;
import com.huydon.reknow.dto.note.NoteRequest;
import com.huydon.reknow.dto.note.NoteResponse;

public interface NoteService {
    NoteResponse createNote(Long bookId, NoteRequest req);

    NoteResponse updateNote(Long id, NoteRequest req);

    void deleteNote(Long id);

    NoteResponse getNoteById (Long id);

    PageResponse<NoteResponse> getNotes (Long bookId, String keyword, int page, int size);
}
