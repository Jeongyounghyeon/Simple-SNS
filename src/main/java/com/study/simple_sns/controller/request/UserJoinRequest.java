package com.study.simple_sns.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class UserJoinRequest {

    @JsonProperty("userName")
    String username;

    @JsonProperty("password")
    String password;
}
