package com.huydon.reknow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY  )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="book_id")
    private Book book;

    @Column(nullable = false, columnDefinition= "TEXT")
    private String content;

    @Builder.Default
    private boolean isSent = false;

    private LocalDateTime deletedAt;

    @UpdateTimestamp
    @Column(nullable=false)
    private LocalDateTime updatedAt;

    @CreationTimestamp
    @Column(nullable=false)
    private LocalDateTime createdAt;
}
