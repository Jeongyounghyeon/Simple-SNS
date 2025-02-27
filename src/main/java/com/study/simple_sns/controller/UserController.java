package com.study.simple_sns.controller;

import com.study.simple_sns.controller.request.UserJoinRequest;
import com.study.simple_sns.controller.response.AlarmResponse;
import com.study.simple_sns.controller.response.Response;
import com.study.simple_sns.controller.response.UserJoinResponse;
import com.study.simple_sns.controller.response.UserLoginResponse;
import com.study.simple_sns.exception.ErrorCode;
import com.study.simple_sns.exception.SimpleSnsException;
import com.study.simple_sns.model.User;
import com.study.simple_sns.service.UserService;
import com.study.simple_sns.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class).orElseThrow(
                () -> new SimpleSnsException(ErrorCode.INTERNAL_SERVER_ERROR,
                        "Casting to User class failed"));

        return Response.success(userService.alarmList(user.getId(), pageable).map(AlarmResponse::fromAlarm));
    }
}
