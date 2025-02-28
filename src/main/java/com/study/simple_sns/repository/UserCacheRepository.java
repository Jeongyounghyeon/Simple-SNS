package com.study.simple_sns.repository;

import com.study.simple_sns.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class UserCacheRepository {

    private final RedisTemplate<String, User> userRedisTemplate;
    private static final Duration USER_CACHE_TTL = Duration.ofDays(3);

    public void setUser(User user) {
        String key = generateKey(user.getUsername());
        log.info("Set User to Redis {}: {}", key, user);
        userRedisTemplate.opsForValue().set(key, user, USER_CACHE_TTL);
    }

    public Optional<User> getUser(String username) {
        String key = generateKey(username);
        User user = userRedisTemplate.opsForValue().get(key);
        log.info("Get data from Redis: {}: {}", key, user);
        return Optional.ofNullable(user);
    }

    private String generateKey(String username) {
        return "USER:" + username;
    }
}
