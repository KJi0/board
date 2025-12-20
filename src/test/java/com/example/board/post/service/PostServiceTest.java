package com.example.board.post.service;

import com.example.board.global.exception.CustomException;
import com.example.board.global.exception.ErrorCode;
import com.example.board.post.domain.Post;
import com.example.board.post.dto.PostUpdateRequest;
import com.example.board.post.repository.PostRepository;
import com.example.board.category.repository.CategoryRepository;
import com.example.board.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    private PostRepository postRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    private PostService postService;

    @BeforeEach
    void setUp() {
        postRepository = mock(PostRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        userRepository = mock(UserRepository.class);

        postService = new PostService(postRepository, categoryRepository, userRepository);
    }

    @Test
    @DisplayName("게시글 수정: 작성자가 아니면 FORBIDDEN(403) CustomException")
    void update_notOwner_throwForbidden() {
        // given
        Long loginUserId = 2L;
        Long postId = 10L;

        Post post = mock(Post.class);
        when(postRepository.findByIdAndIsDeletedFalse(postId)).thenReturn(Optional.of(post));
        when(post.isOwner(loginUserId)).thenReturn(false);

        PostUpdateRequest req = mock(PostUpdateRequest.class);

        // when / then
        assertThatThrownBy(() -> postService.update(loginUserId, postId, req))
                .isInstanceOf(CustomException.class)
                .satisfies(ex -> {
                    CustomException ce = (CustomException) ex;
                    assertThat(ce.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
                    assertThat(ce.getMessage()).isEqualTo(ErrorCode.FORBIDDEN.getMessage());
                });
    }

    @Test
    @DisplayName("게시글 삭제: 작성자면 softDelete가 호출된다(부수효과 검증)")
    void delete_owner_callsSoftDelete() {
        // given
        Long loginUserId = 1L;
        Long postId = 10L;

        Post post = mock(Post.class);
        when(postRepository.findByIdAndIsDeletedFalse(postId)).thenReturn(Optional.of(post));
        when(post.isOwner(loginUserId)).thenReturn(true);

        // when
        postService.delete(loginUserId, postId);

        // then
        verify(post, times(1)).softDeletePost();
    }
}
