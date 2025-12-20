package com.example.board.category.controller;

import com.example.board.category.dto.CategoryCreateRequest;
import com.example.board.category.dto.CategoryUpdateRequest;
import com.example.board.category.dto.CategoryResponse;
import com.example.board.category.service.CategoryService;
import com.example.board.global.response.ApiResponse;
import com.example.board.global.security.AuthUtil;
import com.example.board.global.security.SessionUser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<CategoryResponse>> list() {
        return ApiResponse.ok(categoryService.listActive());
    }

    @PostMapping
    public ApiResponse<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest req, HttpSession session) {
        SessionUser su = AuthUtil.requireLogin(session);
        AuthUtil.requireAdmin(su);
        return ApiResponse.ok(categoryService.create(req));
    }

    @PatchMapping("/{id}")
    public ApiResponse<CategoryResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody CategoryUpdateRequest req,
                                               HttpSession session) {
        SessionUser su = AuthUtil.requireLogin(session);
        AuthUtil.requireAdmin(su);
        return ApiResponse.ok(categoryService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id, HttpSession session) {
        SessionUser su = AuthUtil.requireLogin(session);
        AuthUtil.requireAdmin(su);
        categoryService.deactivate(id);
        return ApiResponse.ok();
    }
}
