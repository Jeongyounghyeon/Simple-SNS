package com.study.simple_sns.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.simple_sns.model.User;
import com.study.simple_sns.model.UserRole;
import lombok.Value;

@Value
public class UserJoinResponse {

    @JsonProperty("id")
    Integer id;

    @JsonProperty("userName")
    String username;

    @JsonProperty("role")
    UserRole role;

    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
