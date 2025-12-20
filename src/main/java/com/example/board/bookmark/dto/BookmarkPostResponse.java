package com.example.board.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookmarkPostResponse {
    private Long postId;
    private String title;
    private LocalDateTime createdAt;
}
