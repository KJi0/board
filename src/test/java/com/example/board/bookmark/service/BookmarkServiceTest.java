package com.example.board.bookmark.service;

import com.example.board.bookmark.repository.BookmarkRepository;
import com.example.board.global.exception.CustomException;
import com.example.board.global.exception.ErrorCode;
import com.example.board.post.domain.Post;
import com.example.board.post.repository.PostRepository;
import com.example.board.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookmarkServiceTest {

    private BookmarkRepository bookmarkRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    private BookmarkService bookmarkService;

    @BeforeEach
    void setUp() {
        bookmarkRepository = mock(BookmarkRepository.class);
        postRepository = mock(PostRepository.class);
        userRepository = mock(UserRepository.class);

        bookmarkService = new BookmarkService(bookmarkRepository, postRepository, userRepository);
    }

    @Test
    @DisplayName("북마크 추가: 이미 북마크한 글이면 DUPLICATE_RESOURCE(409) CustomException")
    void add_duplicate_throwConflict() {
        // given
        Long userId = 1L;
        Long postId = 100L;

        Post post = mock(Post.class);
        when(postRepository.findByIdAndIsDeletedFalse(postId)).thenReturn(Optional.of(post));
        when(bookmarkRepository.existsByUserIdAndPostId(userId, postId)).thenReturn(true);

        // when / then
        assertThatThrownBy(() -> bookmarkService.add(userId, postId))
                .isInstanceOf(CustomException.class)
                .satisfies(ex -> {
                    CustomException ce = (CustomException) ex;
                    assertThat(ce.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_RESOURCE);
                    // 메시지는 서비스에서 detailMessage를 넣었으면 그걸로, 아니면 기본 메시지
                });
    }
}
