package com.huydon.reknow.controller;

import com.huydon.reknow.common.response.ApiResponse;
import com.huydon.reknow.common.response.PageResponse;
import com.huydon.reknow.dto.note.NoteRequest;
import com.huydon.reknow.dto.note.NoteResponse;
import com.huydon.reknow.service.NoteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name="note")
@RequestMapping("/api")
public class NoteController {
    private final NoteService noteService;

    @PostMapping ("/boooks/{bookId}/notes")
    public ResponseEntity <ApiResponse<NoteResponse>> createNote(
            @PathVariable Long bookId, @RequestBody @Valid NoteRequest req)
    {
        ApiResponse<NoteResponse> noteResponse = ApiResponse.success("Create a note successfully",  this.noteService.createNote(bookId, req));
        return ResponseEntity.status(HttpStatus.CREATED).body(noteResponse );
    }

    @GetMapping("/notes/{id}")
    public ResponseEntity<ApiResponse<NoteResponse>> getNote(
            @PathVariable Long id
    ){
        ApiResponse<NoteResponse> noteResponse = ApiResponse.success("Get note successfully",  this.noteService.getNoteById(id));
        return ResponseEntity.ok(noteResponse );
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<ApiResponse<NoteResponse>> updateNote(
            @PathVariable Long id, @RequestBody @Valid NoteRequest req
    ){
        ApiResponse<NoteResponse> noteResponse = ApiResponse.success("Update note successfully",  this.noteService.updateNote(id, req));
        return ResponseEntity.ok(noteResponse);
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<ApiResponse<NoteResponse>> deleteNote(
            @PathVariable Long id
    ){
        this.noteService.deleteNote(id);
        ApiResponse<NoteResponse> noteResponse = ApiResponse.success("Delete note successfully");
        return ResponseEntity.ok(noteResponse);
    }

    @GetMapping("/notes")
    public ResponseEntity<ApiResponse<PageResponse<NoteResponse>>> deleteNote(
            @RequestParam Long bookId, @RequestParam (required=false) String keyword, @RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "5") int size
    ){

        ApiResponse<PageResponse<NoteResponse>> notes= ApiResponse.success("Delete note successfully",this.noteService.getNotes(bookId, keyword, page, size));
        return ResponseEntity.ok(notes);
    }
}
