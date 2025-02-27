package com.study.simple_sns.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.simple_sns.model.Comment;
import lombok.Value;

import java.sql.Timestamp;

@Value
public class CommentResponse {

    @JsonProperty("id")
    Integer id;

    @JsonProperty("comment")
    String comment;

    @JsonProperty("userName")
    String username;

    @JsonProperty("postId")
    Integer postId;

    @JsonProperty("registeredAt")
    Timestamp registeredAt;

    @JsonProperty("updatedAt")
    Timestamp updatedAt;

    @JsonProperty("deletedAt")
    Timestamp deletedAt;

    public static CommentResponse fromComment(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getComment(),
                comment.getUsername(),
                comment.getPostId(),
                comment.getRegisteredAt(),
                comment.getUpdatedAt(),
                comment.getDeletedAt()
        );
    }
}
