package com.example.board.bookmark.controller;

import com.example.board.bookmark.dto.BookmarkPostResponse;
import com.example.board.bookmark.service.BookmarkService;
import com.example.board.global.response.ApiResponse;
import com.example.board.global.security.AuthUtil;
import com.example.board.global.security.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/posts/{postId}/bookmarks")
    public ApiResponse<Void> add(@PathVariable Long postId, HttpSession session) {
        SessionUser su = AuthUtil.requireLogin(session);
        bookmarkService.add(su.getUserId(), postId);
        return ApiResponse.ok();
    }

    @DeleteMapping("/posts/{postId}/bookmarks")
    public ApiResponse<Void> remove(@PathVariable Long postId, HttpSession session) {
        SessionUser su = AuthUtil.requireLogin(session);
        bookmarkService.remove(su.getUserId(), postId);
        return ApiResponse.ok();
    }

    @GetMapping("/me/bookmarks")
    public ApiResponse<List<BookmarkPostResponse>> my(HttpSession session) {
        SessionUser su = AuthUtil.requireLogin(session);
        return ApiResponse.ok(bookmarkService.myBookmarks(su.getUserId()));
    }
}
