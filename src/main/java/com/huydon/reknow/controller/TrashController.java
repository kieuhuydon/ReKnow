package com.huydon.reknow.controller;

import com.huydon.reknow.common.response.ApiResponse;
import com.huydon.reknow.common.response.PageResponse;
import com.huydon.reknow.dto.book.BookResponse;
import com.huydon.reknow.dto.note.NoteResponse;
import com.huydon.reknow.service.TrashService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trash")
@RequiredArgsConstructor
@Tag(name = "Trash")
public class TrashController {
    private final TrashService trashService;

    @GetMapping("/books")
    public ResponseEntity<ApiResponse<PageResponse<BookResponse>>> getDeletedBooks(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue="4") int size
    ){

        PageResponse<BookResponse> books = this.trashService.getDeletedBooks(page, size);
        ApiResponse<PageResponse<BookResponse> > response = ApiResponse.success("Get deleted books successfully", books);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/notes")
    public ResponseEntity<ApiResponse<PageResponse<NoteResponse>>> getDeletedNotes(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue="4") int size
    ){

        PageResponse<NoteResponse> notes = this.trashService.getDeletedNotes(page, size);
        ApiResponse<PageResponse<NoteResponse> > response = ApiResponse.success("Get deleted notes successfully", notes);
        return ResponseEntity.ok(response);

    }

    @PatchMapping("/books/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restoreBook(
            @PathVariable Long id
    ){
        this.trashService.restoreBook(id);
        return ResponseEntity.ok(ApiResponse.success("Restore a book successfully"));
    }

    @PatchMapping("/notes/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restoreNote(
            @PathVariable Long id
    ){
        this.trashService.restoreNote(id);
        return ResponseEntity.ok(ApiResponse.success("Restore a note successfully"));
    }

    @PatchMapping("/notes/restore-all")
    public ResponseEntity<ApiResponse<Void>> restoreAll(){
        this.trashService.restoreAll();
        return ResponseEntity.ok(ApiResponse.success("Restore total books and notes in the trash "));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePermanentBook(
            @PathVariable Long id
    ){
        this.trashService.permanentDeleteBook(id);
        return ResponseEntity.ok(ApiResponse.success("Delete permanent book successfully "));
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePermanentNote(
            @PathVariable Long id
    ){
        this.trashService.permanentDeleteNote(id);
        return ResponseEntity.ok(ApiResponse.success("Delete permanent note successfully "));
    }


}
