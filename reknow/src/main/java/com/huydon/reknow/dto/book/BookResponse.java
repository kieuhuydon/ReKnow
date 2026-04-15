package com.huydon.reknow.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Long id;

    private String title;

    private String author;

    private Integer pubYear;

    private String description;

    private LocalDateTime createdAt;
}
