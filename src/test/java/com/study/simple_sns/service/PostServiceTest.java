package com.study.simple_sns.service;

import com.study.simple_sns.exception.ErrorCode;
import com.study.simple_sns.exception.SimpleSnsException;
import com.study.simple_sns.filxture.PostEntityFixture;
import com.study.simple_sns.filxture.UserEntityFixture;
import com.study.simple_sns.model.entity.PostEntity;
import com.study.simple_sns.model.entity.UserEntity;
import com.study.simple_sns.repository.LikeEntityRepository;
import com.study.simple_sns.repository.PostEntityRepository;
import com.study.simple_sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockitoBean
    private PostEntityRepository postEntityRepository;

    @MockitoBean
    private UserEntityRepository userEntityRepository;

    @MockitoBean
    private LikeEntityRepository likeEntityRepository;

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

    @Test
    public void 포스트삭제가_성공한경우() {
        String username = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(username, postId, 1);
        UserEntity userEntity = postEntity.getUser();

        // mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        assertDoesNotThrow(() -> postService.delete(username, postId));
    }

    @Test
    public void 포스트삭제시_포스트가_존재하지_않는_경우() {
        String username = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(username, postId, 1);
        UserEntity userEntity = postEntity.getUser();

        // mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        SimpleSnsException e = assertThrows(SimpleSnsException.class, () -> postService.delete(username, postId));
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    public void 포스트삭제시_권한이_없는_경우() {
        String username = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(username, postId, 1);
        UserEntity writer = UserEntityFixture.get("username1", "password", 1);

        // mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        SimpleSnsException e = assertThrows(SimpleSnsException.class, () -> postService.delete(username, postId));
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    public void 피드목록요청이_성공한경우() {
        Pageable pageable = mock(Pageable.class);

        when(postEntityRepository.findAll(pageable)).thenReturn(Page.empty());

        assertDoesNotThrow(() -> postService.list(pageable));
    }

    @Test
    public void 내피드목록요청이_성공한경우() {
        Pageable pageable = mock(Pageable.class);
        UserEntity userEntity = mock(UserEntity.class);

        when(userEntityRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findAllByUser(userEntity, pageable)).thenReturn(Page.empty());

        assertDoesNotThrow(() -> postService.my("", pageable));
    }

    @Test
    public void 좋아요_요청이_성공한_경우() throws Exception {
        Integer postId = 1;
        String username = "username";

        PostEntity postEntity = PostEntityFixture.get(username, postId, 1);

        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(likeEntityRepository.existsByUserAndPost(any(), any())).thenReturn(false);

        assertDoesNotThrow(() -> postService.like(postId, username));
    }

    @Test
    public void 좋아요_요청시_게시물이_없는경우() throws Exception {
        Integer postId = 1;
        String username = "username";

        doThrow(new SimpleSnsException(ErrorCode.POST_NOT_FOUND)).when(postEntityRepository).findById(postId);

        SimpleSnsException e = assertThrows(SimpleSnsException.class, () -> postService.like(postId, username));
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    public void 좋아요_요청시_이미_좋아요를_누른_게시물인_경우() throws Exception {
        Integer postId = 1;
        String username = "username";

        PostEntity postEntity = PostEntityFixture.get(username, postId, 1);

        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(likeEntityRepository.existsByUserAndPost(any(), any())).thenReturn(true);

        SimpleSnsException e = assertThrows(SimpleSnsException.class, () -> postService.like(postId, username));
        assertEquals(ErrorCode.ALREADY_LIKED, e.getErrorCode());
    }
}
