package com.malgn.entity;

import com.malgn.auth.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(nullable = false, unique = true, length = 50)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(updatable = false, nullable = false)
    private LocalDateTime registerDate;

    @Column(length = 50, updatable = false, nullable = false)
    private String registerBy;

    @Builder
    public User(String userId, String password, Role role, LocalDateTime registerDate, String registerBy) {
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.registerDate = registerDate;
        this.registerBy = registerBy;
    }
}
