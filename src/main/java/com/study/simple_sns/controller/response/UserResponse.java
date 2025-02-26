package com.study.simple_sns.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.simple_sns.model.User;
import com.study.simple_sns.model.UserRole;
import lombok.Value;

@Value
public class UserResponse {

    @JsonProperty("id")
    Integer id;

    @JsonProperty("userName")
    String username;

    @JsonProperty("role")
    UserRole role;

    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
