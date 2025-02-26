package com.study.simple_sns.filxture;

import com.study.simple_sns.model.entity.PostEntity;
import com.study.simple_sns.model.entity.UserEntity;

public class PostEntityFixture {

    public static PostEntity get(String username, Integer postId, Integer userId) {
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername(username);

        PostEntity result = new PostEntity();
        result.setId(postId);
        result.setUser(user);

        return result;
    }
}
