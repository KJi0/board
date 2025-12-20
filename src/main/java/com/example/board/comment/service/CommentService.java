package com.example.board.comment.service;

import com.example.board.comment.domain.Comment;
import com.example.board.comment.dto.CommentCreateRequest;
import com.example.board.comment.dto.CommentUpdateRequest;
import com.example.board.comment.dto.CommentResponse;
import com.example.board.comment.repository.CommentRepository;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentResponse createTopLevel(Long loginUserId, Long postId, CommentCreateRequest req) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        User author = userRepository.findById(loginUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment saved = commentRepository.save(
                Comment.builder()
                        .post(post)
                        .author(author)
                        .parent(null)
                        .content(req.getContent())
                        .isDeleted(false)
                        .build()
        );

        return toDto(saved);
    }

    public CommentResponse createReply(Long loginUserId, Long parentCommentId, CommentCreateRequest req) {
        Comment parent = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        Post post = parent.getPost();
        if (post.isDeleted()) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }

        User author = userRepository.findById(loginUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment saved = commentRepository.save(
                Comment.builder()
                        .post(post)
                        .author(author)
                        .parent(parent)
                        .content(req.getContent())
                        .isDeleted(false)
                        .build()
        );

        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> listByPost(Long postId) {
        // 게시글 존재 확인(삭제 포함 제외)
        postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public CommentResponse update(Long loginUserId, Long commentId, CommentUpdateRequest req) {
        Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.isOwner(loginUserId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        comment.updateContent(req.getContent());
        return toDto(comment);
    }

    public void delete(Long loginUserId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.isOwner(loginUserId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 소프트 삭제 + "삭제된 댓글입니다"
        comment.softDeleteComment();
    }

    private CommentResponse toDto(Comment c) {
        return new CommentResponse(
                c.getId(),
                c.getPost().getId(),
                c.getAuthor().getId(),
                c.getParent() == null ? null : c.getParent().getId(),
                c.getContent(),
                c.isDeleted(),
                c.getCreatedAt()
        );
    }
}
