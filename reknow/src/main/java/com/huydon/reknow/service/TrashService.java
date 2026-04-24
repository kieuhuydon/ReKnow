package com.huydon.reknow.service;

import com.huydon.reknow.common.response.PageResponse;
import com.huydon.reknow.dto.book.BookResponse;
import com.huydon.reknow.dto.note.NoteResponse;

public interface TrashService {
    PageResponse<BookResponse> getDeletedBooks(int page, int size);

    PageResponse<NoteResponse> getDeletedNotes (int page, int size);

    void restoreBook(Long id);

    void restoreNote(Long id);

    void restoreAll(); // khôi phục tất

    void permanentDeleteNote(Long id);

    void permanentDeleteBook (Long id);
}
