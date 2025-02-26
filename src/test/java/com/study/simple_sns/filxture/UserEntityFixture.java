package com.study.simple_sns.filxture;

import com.study.simple_sns.model.entity.UserEntity;

public class UserEntityFixture {

    public static UserEntity get(String username, String password, Integer userId) {
        UserEntity result = new UserEntity();
        result.setId(userId);
        result.setUsername(username);
        result.setPassword(password);

        return result;
    }
}
