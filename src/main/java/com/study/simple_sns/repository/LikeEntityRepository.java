package com.study.simple_sns.repository;

import com.study.simple_sns.model.entity.LikeEntity;
import com.study.simple_sns.model.entity.PostEntity;
import com.study.simple_sns.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {

    boolean existsByUserAndPost(UserEntity user, PostEntity post);

//    @Query("SELECT COUNT(*) FROM LikeEntity entity WHERE entity.post = :post")
//    Integer countByPost(@Param("post") PostEntity post);

    long countByPost(PostEntity post);

    List<LikeEntity> findAllByPost(PostEntity post);

    @Transactional
    @Modifying
    @Query("UPDATE LikeEntity entity SET entity.deletedAt = CURRENT_TIMESTAMP() WHERE entity.post = :post")
    void deleteByAllPost(@Param("post") PostEntity post);
}
