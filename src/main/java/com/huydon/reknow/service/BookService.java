package com.huydon.reknow.service;

import com.huydon.reknow.common.response.PageResponse;
import com.huydon.reknow.dto.book.BookRequest;
import com.huydon.reknow.dto.book.BookResponse;

public interface BookService {
    public BookResponse createBook(BookRequest req);
    public PageResponse<BookResponse> getBooks(String keyword, int page, int size, String sortBy, String direction);
    public BookResponse updateBook(Long id, BookRequest req);
    public void deleteBook (Long id);
    public BookResponse getBookById (Long id );
}
