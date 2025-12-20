package com.example.board.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CategoryUpdateRequest {
    @NotBlank
    @Size(max = 50)
    private String name;
}
