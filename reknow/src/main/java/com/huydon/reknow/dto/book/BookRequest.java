package com.huydon.reknow.dto.book;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    @NotBlank (message =" Title is required")
    private String title;

    private String author;


    private Integer pubYear;

    private String description;
}
