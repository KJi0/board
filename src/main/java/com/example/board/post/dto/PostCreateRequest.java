package com.example.board.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostCreateRequest {
    @NotNull
    private Long categoryId;

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    private String content;
}
