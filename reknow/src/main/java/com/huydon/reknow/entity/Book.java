package com.huydon.reknow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable=false)
    private String title;

    @OneToMany (mappedBy= "book")
    private List<Note> notes;

    private String author;

    private Integer pubYear;

    @Column(columnDefinition= "TEXT")
    private String description;

    @CreationTimestamp
    @Column (nullable=false, updatable=false)
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    @UpdateTimestamp
    @Column (nullable=false)
    private LocalDateTime updatedAt;
}
