package com.example.board.category.repository;

import com.example.board.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    List<Category> findByIsActiveTrueOrderByNameAsc();
}
