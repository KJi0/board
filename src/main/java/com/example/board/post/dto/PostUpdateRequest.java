package com.example.board.post.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostUpdateRequest {
    @NotNull
    private Long categoryId;

    @Size(max = 200)
    private String title;

    private String content;
}
