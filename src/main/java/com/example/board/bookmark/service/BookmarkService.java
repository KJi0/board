package com.example.board.bookmark.service;

import com.example.board.bookmark.domain.Bookmark;
import com.example.board.bookmark.dto.BookmarkPostResponse;
import com.example.board.bookmark.repository.BookmarkRepository;
import com.example.board.global.exception.CustomException;
import com.example.board.global.exception.ErrorCode;
import com.example.board.post.domain.Post;
import com.example.board.post.repository.PostRepository;
import com.example.board.user.domain.User;
import com.example.board.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void add(Long loginUserId, Long postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (bookmarkRepository.existsByUserIdAndPostId(loginUserId, postId)) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "이미 북마크한 게시글입니다.");
        }

        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        bookmarkRepository.save(
                Bookmark.builder()
                        .user(user)
                        .post(post)
                        .build()
        );
    }

    public void remove(Long loginUserId, Long postId) {
        Bookmark bm = bookmarkRepository.findByUserIdAndPostId(loginUserId, postId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REQUEST, "북마크가 존재하지 않습니다."));

        bookmarkRepository.delete(bm);
    }

    @Transactional(readOnly = true)
    public List<BookmarkPostResponse> myBookmarks(Long loginUserId) {
        // post가 소프트삭제면 목록에서 제외
        return bookmarkRepository.findByUserIdOrderByCreatedAtDesc(loginUserId)
                .stream()
                .filter(b -> !b.getPost().isDeleted())
                .map(b -> new BookmarkPostResponse(b.getPost().getId(), b.getPost().getTitle(), b.getPost().getCreatedAt()))
                .toList();
    }
}
