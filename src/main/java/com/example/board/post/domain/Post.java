package com.example.board.post.domain;

import com.example.board.category.domain.Category;
import com.example.board.global.BaseEntity;
import com.example.board.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(length = 200, nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void update(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public void softDeletePost() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isOwner(Long userId) {
        return this.author != null && this.author.getId().equals(userId);
    }
}
