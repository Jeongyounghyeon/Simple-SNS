package com.study.simple_sns.repository;

import com.study.simple_sns.model.entity.CommentEntity;
import com.study.simple_sns.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {

    Page<CommentEntity> findAllByPost(PostEntity postEntity, Pageable pageable);


    @Transactional
    @Modifying
    @Query("UPDATE CommentEntity entity SET entity.deletedAt = CURRENT_TIMESTAMP() WHERE entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity postEntity);
}
