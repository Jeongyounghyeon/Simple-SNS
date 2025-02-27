package com.study.simple_sns.repository;

import com.study.simple_sns.model.entity.CommentEntity;
import com.study.simple_sns.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {

    Page<CommentEntity> findAllByPost(PostEntity postEntity, Pageable pageable);
}
