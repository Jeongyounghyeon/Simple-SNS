package com.study.simple_sns.service;

import com.study.simple_sns.exception.ErrorCode;
import com.study.simple_sns.exception.SimpleSnsException;
import com.study.simple_sns.model.Post;
import com.study.simple_sns.model.entity.PostEntity;
import com.study.simple_sns.model.entity.UserEntity;
import com.study.simple_sns.repository.PostEntityRepository;
import com.study.simple_sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Transactional
    public void create(String title, String body, String username) {
        // user find
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() ->
                new SimpleSnsException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        // post save
        postEntityRepository.save(PostEntity.of(title, body, userEntity));
    }

    @Transactional
    public Post modify(String title, String body, String username, Integer postId) {
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() ->
                new SimpleSnsException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        // post exist
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SimpleSnsException(ErrorCode.POST_NOT_FOUND, String.format("%d not founded", postId)));

        // post permission
        if (postEntity.getUser() != userEntity) {
            throw new SimpleSnsException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", username, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(String username, Integer postId) {
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() ->
                new SimpleSnsException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SimpleSnsException(ErrorCode.POST_NOT_FOUND, String.format("%d not founded", postId)));

        if (postEntity.getUser() != userEntity) {
            throw new SimpleSnsException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", username, postId));
        }

        postEntityRepository.delete(postEntity);
    }
}
