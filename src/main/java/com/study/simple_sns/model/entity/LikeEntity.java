package com.study.simple_sns.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "\"like\"")
@Getter
@Setter
@SQLDelete(sql = "UPDATE \"like\" SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void prePersist() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static LikeEntity of(UserEntity user, PostEntity post) {
        LikeEntity postEntity = new LikeEntity();
        postEntity.setPost(post);
        postEntity.setUser(user);
        postEntity.setUser(user);
        return postEntity;
    }
}
