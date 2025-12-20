package com.example.board.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private Long postId;
    private Long authorId;
    private Long parentId; // null이면 최상위
    private String content;
    private boolean deleted;
    private LocalDateTime createdAt;
}
