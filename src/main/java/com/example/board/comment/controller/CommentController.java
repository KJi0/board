package com.example.board.comment.controller;

import com.example.board.comment.dto.CommentCreateRequest;
import com.example.board.comment.dto.CommentUpdateRequest;
import com.example.board.comment.dto.CommentResponse;
import com.example.board.comment.service.CommentService;
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
public class CommentController {

    private final CommentService commentService;

    // 최상위 댓글 작성
    @PostMapping("/posts/{postId}/comments")
    public ApiResponse<CommentResponse> createComment(@PathVariable Long postId,
                                                      @Valid @RequestBody CommentCreateRequest req,
                                                      HttpSession session) {
        SessionUser su = AuthUtil.requireLogin(session);
        return ApiResponse.ok(commentService.createTopLevel(su.getUserId(), postId, req));
    }

    // 대댓글 작성
    @PostMapping("/comments/{commentId}/replies")
    public ApiResponse<CommentResponse> createReply(@PathVariable Long commentId,
                                                    @Valid @RequestBody CommentCreateRequest req,
                                                    HttpSession session) {
        SessionUser su = AuthUtil.requireLogin(session);
        return ApiResponse.ok(commentService.createReply(su.getUserId(), commentId, req));
    }

    // 게시글의 댓글 목록
    @GetMapping("/posts/{postId}/comments")
    public ApiResponse<List<CommentResponse>> list(@PathVariable Long postId) {
        return ApiResponse.ok(commentService.listByPost(postId));
    }

    @PatchMapping("/comments/{id}")
    public ApiResponse<CommentResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody CommentUpdateRequest req,
                                               HttpSession session) {
        SessionUser su = AuthUtil.requireLogin(session);
        return ApiResponse.ok(commentService.update(su.getUserId(), id, req));
    }

    @DeleteMapping("/comments/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id, HttpSession session) {
        SessionUser su = AuthUtil.requireLogin(session);
        commentService.delete(su.getUserId(), id);
        return ApiResponse.ok();
    }
}
