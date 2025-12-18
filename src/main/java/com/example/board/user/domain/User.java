package com.example.board.user.domain;

import com.example.board.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user",
        uniqueConstraints = {
            @UniqueConstraint(name = "uq_user_username", columnNames = "username"),
            @UniqueConstraint(name = "uq_user_email", columnNames = "email")
        })
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String username;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Role role;

    public void changePasswordHash(String newHash) {
        this.passwordHash = newHash;
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
}
