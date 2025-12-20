package com.example.board.category.service;

import com.example.board.category.domain.Category;
import com.example.board.category.dto.CategoryCreateRequest;
import com.example.board.category.dto.CategoryUpdateRequest;
import com.example.board.category.dto.CategoryResponse;
import com.example.board.category.repository.CategoryRepository;
import com.example.board.global.exception.CustomException;
import com.example.board.global.exception.ErrorCode;
import com.example.board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> listActive() {
        return categoryRepository.findByIsActiveTrueOrderByNameAsc()
                .stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .toList();
    }

    public CategoryResponse create(CategoryCreateRequest req) {
        if (categoryRepository.existsByName(req.getName())) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "이미 존재하는 카테고리입니다.");
        }
        Category saved = categoryRepository.save(
                Category.builder()
                        .name(req.getName())
                        .isActive(true)
                        .build()
        );
        return new CategoryResponse(saved.getId(), saved.getName());
    }

    public CategoryResponse update(Long id, CategoryUpdateRequest req) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        if (!category.isActive()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "비활성화된 카테고리입니다.");
        }
        if (!category.getName().equals(req.getName()) && categoryRepository.existsByName(req.getName())) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "이미 존재하는 카테고리명입니다.");
        }

        category.rename(req.getName());
        return new CategoryResponse(category.getId(), category.getName());
    }

    public void deactivate(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        // 게시글이 하나라도 있으면 삭제(비활성화) 불가
        if (postRepository.existsByCategoryIdAndIsDeletedFalse(id)) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "해당 카테고리에 게시글이 있어 비활성화할 수 없습니다.");
        }

        category.deactivate();
    }
}
