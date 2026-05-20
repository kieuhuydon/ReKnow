package com.huydon.reknow.entity;

import com.huydon.reknow.entity.enums.Provider;
import com.huydon.reknow.entity.enums.Role;
import com.huydon.reknow.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@OneToMany (mappedBy = "user")
    private List<Book> books;
    không cần, chỉ cần khi gọi user.getBoooks()
     */

    @Column(nullable=false)
    private String name;

    @Column(nullable=false, unique = true)
    private String email;

    private String password;

    private String providerId;

    @Enumerated (EnumType.STRING)
    private Role role;

    @Enumerated (EnumType.STRING)
    private Status status;

    @Enumerated (EnumType.STRING)
    private Provider provider;

    @CreationTimestamp
    @Column (updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column (nullable = false)
    private LocalDateTime updatedAt;



}
