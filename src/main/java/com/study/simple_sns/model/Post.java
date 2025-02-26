package com.study.simple_sns.model;

import com.study.simple_sns.model.entity.PostEntity;
import com.study.simple_sns.model.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Value;

import java.sql.Timestamp;

@Value
public class Post {

    Integer id;
    String title;
    String body;
    User user;
    Timestamp registeredAt;
    Timestamp updatedAt;
    Timestamp deletedAt;

    public static Post fromEntity(PostEntity entity) {
        return new Post(
                entity.getId(),
                entity.getTitle(),
                entity.getBody(),
                User.fromEntity(entity.getUser()),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
