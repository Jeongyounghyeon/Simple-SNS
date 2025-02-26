package com.study.simple_sns.controller.request;

import lombok.Value;

@Value
public class PostCreateRequest {

    String title;
    String body;
}
