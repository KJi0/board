package com.example.board.comment.service;

import com.example.board.comment.domain.Comment;
import com.example.board.comment.repository.CommentRepository;
import com.example.board.global.exception.CustomException;
import com.example.board.global.exception.ErrorCode;
import com.example.board.post.repository.PostRepository;
import com.example.board.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    private CommentService commentService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        postRepository = mock(PostRepository.class);
        userRepository = mock(UserRepository.class);

        commentService = new CommentService(commentRepository, postRepository, userRepository);
    }

    @Test
    @DisplayName("댓글 삭제: 작성자면 softDeleteAsMasked가 호출된다(삭제된 댓글입니다 정책)")
    void delete_owner_callsMaskedDelete() {
        // given
        Long loginUserId = 1L;
        Long commentId = 10L;

        Comment comment = mock(Comment.class);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(comment.isOwner(loginUserId)).thenReturn(true);

        // when
        commentService.delete(loginUserId, commentId);

        // then
        verify(comment, times(1)).softDeleteComment();
    }

    @Test
    @DisplayName("댓글 수정: 작성자가 아니면 FORBIDDEN(403)")
    void update_notOwner_throwForbidden() {
        // given
        Long loginUserId = 2L;
        Long commentId = 10L;

        Comment comment = mock(Comment.class);
        when(commentRepository.findByIdAndIsDeletedFalse(commentId)).thenReturn(Optional.of(comment));
        when(comment.isOwner(loginUserId)).thenReturn(false);

        // when / then
        assertThatThrownBy(() -> commentService.update(loginUserId, commentId, null))
                .isInstanceOf(CustomException.class)
                .satisfies(ex -> {
                    CustomException ce = (CustomException) ex;
                    assertThat(ce.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
                });
    }
}
