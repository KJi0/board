package com.example.board.category.domain;

import com.example.board.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category",
    uniqueConstraints = @UniqueConstraint(name = "uq_category_name", columnNames = "name"))
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean isActive;

    public void rename(String name) {
        this.name = name;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }
}
