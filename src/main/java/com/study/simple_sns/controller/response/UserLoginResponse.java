package com.study.simple_sns.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class UserLoginResponse {

    @JsonProperty("token")
    String token;
}
