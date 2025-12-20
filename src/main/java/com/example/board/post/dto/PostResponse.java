package com.example.board.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private Long authorId;
    private Long categoryId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
