package com.huydon.reknow.controller;

import com.huydon.reknow.common.response.ApiResponse;
import com.huydon.reknow.common.response.PageResponse;
import com.huydon.reknow.dto.book.BookRequest;
import com.huydon.reknow.dto.book.BookResponse;
import com.huydon.reknow.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Book")
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponse>> createBook(
            @RequestBody @Valid BookRequest req)
    {
        ApiResponse<BookResponse> res = ApiResponse.success("Create a book successfully", bookService.createBook(req));
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(
            @RequestBody @Valid BookRequest req, @PathVariable Long id
    ){
        ApiResponse<BookResponse> res = ApiResponse.success("Update a book successfully", bookService.updateBook(id, req));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(
            @PathVariable Long id
    ){
        bookService.deleteBook(id);
        ApiResponse<Void> res = ApiResponse.success("Delete a book successfully ");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(
            @PathVariable Long id
    ){
        ApiResponse<BookResponse> res = ApiResponse.success("Get a book successfully ", bookService.getBookById(id));
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BookResponse>>> getBooks(
            @RequestParam (required = false)String keyword,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        ApiResponse<PageResponse<BookResponse>> res = ApiResponse.success("Get  books successfully ", bookService.getBooks(keyword,page, size, sortBy, direction));
        return ResponseEntity.ok(res);
    }
}
