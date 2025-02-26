package com.study.simple_sns.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Response<T> {

    @JsonProperty("resultCode")
    String resultCode;

    @JsonProperty("result")
    T result;

    public static <T> Response<T> error(String errorCode) {
        return new Response<>(errorCode, null);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>("SUCCESS", result);
    }
}
