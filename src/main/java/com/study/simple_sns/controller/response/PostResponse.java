package com.study.simple_sns.controller.response;

import com.study.simple_sns.model.Post;
import com.study.simple_sns.model.User;
import lombok.Value;

import java.sql.Timestamp;

@Value
public class PostResponse {

    Integer id;
    String title;
    String body;
    UserResponse user;
    Timestamp registeredAt;
    Timestamp updatedAt;
    Timestamp deletedAt;

    public static PostResponse fromPost(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                UserResponse.fromUser(post.getUser()),
                post.getRegisteredAt(),
                post.getUpdatedAt(),
                post.getDeletedAt()
        );
    }
}
