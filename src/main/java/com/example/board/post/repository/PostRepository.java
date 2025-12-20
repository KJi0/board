package com.example.board.post.repository;

import com.example.board.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
    Optional<Post> findByIdAndIsDeletedFalse(Long id);
    boolean existsByCategoryIdAndIsDeletedFalse(Long categoryId);
}
