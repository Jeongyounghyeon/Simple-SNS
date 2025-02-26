package com.study.simple_sns.service;

import com.study.simple_sns.SimpleSnsApplication;
import com.study.simple_sns.exception.ErrorCode;
import com.study.simple_sns.exception.SimpleSnsException;
import com.study.simple_sns.filxture.PostEntityFixture;
import com.study.simple_sns.filxture.UserEntityFixture;
import com.study.simple_sns.model.entity.PostEntity;
import com.study.simple_sns.model.entity.UserEntity;
import com.study.simple_sns.repository.PostEntityRepository;
import com.study.simple_sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockitoBean
    private PostEntityRepository postEntityRepository;

    @MockitoBean
    private UserEntityRepository userEntityRepository;

    @Test
    public void 포스트작성이_성공한경우() {
        String title = "title";
        String body = "body";
        String username = "username";

        // mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        assertDoesNotThrow(() -> postService.create(title, body, username));
    }

    @Test
    public void 포스트작성시_요청한유저가_존재하지않는경우() {
        String title = "title";
        String body = "body";
        String username = "username";

        // mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        SimpleSnsException e = assertThrows(SimpleSnsException.class, () -> postService.create(title, body, username));
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    public void 포스트수정이_성공한경우() {
        String title = "title";
        String body = "body";
        String username = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(username, postId, 1);
        UserEntity userEntity = postEntity.getUser();

        // mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(postEntityRepository.saveAndFlush(postEntity)).thenReturn(postEntity);

        assertDoesNotThrow(() -> postService.modify(title, body, username, postId));
    }

    @Test
    public void 포스트수정시_포스트가_존재하지_않는_경우() {
        String title = "title";
        String body = "body";
        String username = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(username, postId, 1);
        UserEntity userEntity = postEntity.getUser();

        // mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        SimpleSnsException e = assertThrows(SimpleSnsException.class, () -> postService.modify(title, body, username, postId));
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    public void 포스트수정시_권한이_없는_경우() {
        String title = "title";
        String body = "body";
        String username = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(username, postId, 1);
        UserEntity writer = UserEntityFixture.get("username1", "password", 1);

        // mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        SimpleSnsException e = assertThrows(SimpleSnsException.class, () -> postService.modify(title, body, username, postId));
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }
}
