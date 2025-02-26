package com.study.simple_sns.controller;

import com.study.simple_sns.controller.request.UserJoinRequest;
import com.study.simple_sns.controller.response.Response;
import com.study.simple_sns.controller.response.UserJoinResponse;
import com.study.simple_sns.controller.response.UserLoginResponse;
import com.study.simple_sns.model.User;
import com.study.simple_sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getUsername(), request.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserJoinRequest request) {
        String token = userService.login(request.getUsername(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }
}
