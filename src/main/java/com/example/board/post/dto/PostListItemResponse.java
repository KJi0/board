package com.example.board.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostListItemResponse {
    private Long id;
    private Long categoryId;
    private String title;
    private LocalDateTime createdAt;
}
