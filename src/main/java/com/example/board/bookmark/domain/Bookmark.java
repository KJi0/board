package com.example.board.bookmark.domain;

import com.example.board.global.BaseEntity;
import com.example.board.post.domain.Post;
import com.example.board.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bookmark",
        uniqueConstraints = @UniqueConstraint(name = "uq_bookmark_user_post", columnNames = {"user_id", "post_id"}))
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}