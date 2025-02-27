package com.study.simple_sns.service;

import com.study.simple_sns.exception.ErrorCode;
import com.study.simple_sns.exception.SimpleSnsException;
import com.study.simple_sns.model.Post;
import com.study.simple_sns.model.entity.LikeEntity;
import com.study.simple_sns.model.entity.PostEntity;
import com.study.simple_sns.model.entity.UserEntity;
import com.study.simple_sns.repository.LikeEntityRepository;
import com.study.simple_sns.repository.PostEntityRepository;
import com.study.simple_sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;

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

    public Page<Post> list(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String username, Pageable pageable) {
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() ->
                new SimpleSnsException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void like(Integer postId, String username) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SimpleSnsException(ErrorCode.POST_NOT_FOUND, String.format("%d not founded", postId)));

        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() ->
                new SimpleSnsException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        // check liked -> throw
        if (likeEntityRepository.existsByUserAndPost(userEntity, postEntity)) {
            throw new SimpleSnsException(ErrorCode.ALREADY_LIKED, String.format("username %s already like post %d", username, postId));
        }

        // like save
        likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));
    }

    public int likeCount(Integer postId) {
        // post exist
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SimpleSnsException(ErrorCode.POST_NOT_FOUND, String.format("%d not founded", postId)));

        // count like
        return likeEntityRepository.countByPost(postEntity);
    }
}
