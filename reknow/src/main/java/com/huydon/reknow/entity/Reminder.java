package com.huydon.reknow.entity;

import com.huydon.reknow.entity.enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reminders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "user_id", nullable = false)
    private User user;

    //book Id nullable
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "book_id")
    private Book book;

    @Enumerated (EnumType.STRING)
    private Type type;

    @Builder.Default
    private Boolean active = true;

    private LocalTime sendTime; // giờ phút

    private LocalDateTime lastSentAt; // ngày giờ

    private Long lastSentNoteId;

    @CreationTimestamp
    private LocalDateTime createdAt;


}
