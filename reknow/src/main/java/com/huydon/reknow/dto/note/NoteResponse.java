package com.huydon.reknow.dto.note;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponse {
    private Long id;
    private Long bookId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
