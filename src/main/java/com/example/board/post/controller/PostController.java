package com.example.board.post.controller;

import com.example.board.global.response.ApiResponse;
import com.example.board.global.security.AuthUtil;
import com.example.board.global.security.SessionUser;
import com.example.board.post.dto.PostCreateRequest;
import com.example.board.post.dto.PostUpdateRequest;
import com.example.board.post.dto.PostListItemResponse;
import com.example.board.post.dto.PostResponse;
import com.example.board.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ApiResponse<PostResponse> create(@Valid @RequestBody PostCreateRequest req, HttpSession session) {
        SessionUser su = AuthUtil.requireLogin(session);
        return ApiResponse.ok(postService.create(su.getUserId(), req));
    }

    @GetMapping
    public ApiResponse<Page<PostListItemResponse>> list(Pageable pageable) {
        return ApiResponse.ok(postService.list(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<PostResponse> get(@PathVariable Long id) {
        return ApiResponse.ok(postService.get(id));
    }

    @PatchMapping("/{id}")
    public ApiResponse<PostResponse> update(@PathVariable Long id,
                                            @Valid @RequestBody PostUpdateRequest req,
                                            HttpSession session) {
        SessionUser su = AuthUtil.requireLogin(session);
        return ApiResponse.ok(postService.update(su.getUserId(), id, req));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id, HttpSession session) {
        SessionUser su = AuthUtil.requireLogin(session);
        postService.delete(su.getUserId(), id);
        return ApiResponse.ok();
    }
}
