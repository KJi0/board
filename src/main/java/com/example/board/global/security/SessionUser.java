package com.example.board.global.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionUser {
    private final Long userId;
    private final String role; // "USER" or "ADMIN"
}
