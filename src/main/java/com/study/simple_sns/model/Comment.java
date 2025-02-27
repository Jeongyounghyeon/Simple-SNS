package com.study.simple_sns.model;

import com.study.simple_sns.model.entity.CommentEntity;
import com.study.simple_sns.model.entity.PostEntity;
import lombok.Value;

import java.sql.Timestamp;

@Value
public class Comment {

    Integer id;
    String comment;
    String username;
    Integer postId;
    Timestamp registeredAt;
    Timestamp updatedAt;
    Timestamp deletedAt;

    public static Comment fromEntity(CommentEntity entity) {
        return new Comment(
                entity.getId(),
                entity.getComment(),
                entity.getUser().getUsername(),
                entity.getPost().getId(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
