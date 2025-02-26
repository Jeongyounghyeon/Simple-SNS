package com.study.simple_sns.model;

import com.study.simple_sns.model.entity.UserEntity;
import lombok.Value;

import java.sql.Timestamp;

@Value
public class User {

    Integer id;
    String username;
    String password;
    UserRole role;
    Timestamp registeredAt;
    Timestamp updatedAt;
    Timestamp deletedAt;

    public static User fromEntity(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getRole(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
