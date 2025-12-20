package com.example.board.post.service;

import com.example.board.category.domain.Category;
import com.example.board.category.repository.CategoryRepository;
import com.example.board.global.exception.CustomException;
import com.example.board.global.exception.ErrorCode;
import com.example.board.post.domain.Post;
import com.example.board.post.dto.PostCreateRequest;
import com.example.board.post.dto.PostUpdateRequest;
import com.example.board.post.dto.PostListItemResponse;
import com.example.board.post.dto.PostResponse;
import com.example.board.post.repository.PostRepository;
import com.example.board.user.domain.User;
import com.example.board.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public PostResponse create(Long loginUserId, PostCreateRequest req) {
        User author = userRepository.findById(loginUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        if (!category.isActive()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "비활성화된 카테고리에는 글을 작성할 수 없습니다.");
        }

        Post saved = postRepository.save(
                Post.builder()
                        .author(author)
                        .category(category)
                        .title(req.getTitle())
                        .content(req.getContent())
                        .isDeleted(false)
                        .build()
        );

        return new PostResponse(saved.getId(), author.getId(), category.getId(),
                saved.getTitle(), saved.getContent(), saved.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public Page<PostListItemResponse> list(Pageable pageable) {
        return postRepository.findByIsDeletedFalseOrderByCreatedAtDesc(pageable)
                .map(p -> new PostListItemResponse(p.getId(), p.getCategory().getId(), p.getTitle(), p.getCreatedAt()));
    }

    @Transactional(readOnly = true)
    public PostResponse get(Long id) {
        Post post = postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return new PostResponse(post.getId(), post.getAuthor().getId(), post.getCategory().getId(),
                post.getTitle(), post.getContent(), post.getCreatedAt());
    }

    public PostResponse update(Long loginUserId, Long postId, PostUpdateRequest req) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.isOwner(loginUserId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        if (!category.isActive()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "비활성화된 카테고리로 변경할 수 없습니다.");
        }

        // null 허용: 값이 들어온 것만 수정
        String title = (req.getTitle() != null) ? req.getTitle() : post.getTitle();
        String content = (req.getContent() != null) ? req.getContent() : post.getContent();

        post.update(title, content, category);

        return new PostResponse(post.getId(), post.getAuthor().getId(), post.getCategory().getId(),
                post.getTitle(), post.getContent(), post.getCreatedAt());
    }

    public void delete(Long loginUserId, Long postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.isOwner(loginUserId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        post.softDeletePost();
    }
}
