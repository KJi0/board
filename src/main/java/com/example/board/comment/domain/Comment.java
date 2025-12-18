package com.example.board.comment.domain;

import com.example.board.global.BaseEntity;
import com.example.board.post.domain.Post;
import com.example.board.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public boolean isReply() {
        return this.parent != null;
    }

    public boolean isOwner(Long userId) {
        return this.author != null && this.author.getId().equals(userId);
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void softDeleteComment() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.content = "삭제된 댓글입니다";
    }
}