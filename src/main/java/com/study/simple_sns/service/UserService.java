package com.study.simple_sns.service;

import com.study.simple_sns.exception.ErrorCode;
import com.study.simple_sns.exception.SimpleSnsException;
import com.study.simple_sns.model.Alarm;
import com.study.simple_sns.model.User;
import com.study.simple_sns.model.entity.AlarmEntity;
import com.study.simple_sns.model.entity.UserEntity;
import com.study.simple_sns.repository.AlarmEntityRepository;
import com.study.simple_sns.repository.UserEntityRepository;
import com.study.simple_sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public User loadUserByUsername(String username) {
        return userEntityRepository.findByUsername(username)
                .map(User::fromEntity)
                .orElseThrow(() -> new SimpleSnsException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
    }

    @Transactional
    public User join(String username, String password) {
        // 회원가입하려는 username으로 회원가입한 user가 있는지 확인
        userEntityRepository.findByUsername(username).ifPresent(it -> {
            throw new SimpleSnsException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", username));
        });

        // 없으면 회원가입 진행
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(username, encoder.encode(password)));
        return User.fromEntity(userEntity);
    }

    @Transactional
    public String login(String username, String password) {
        // 회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() -> new SimpleSnsException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        // 비밀번호 체크

        if (!encoder.matches(password, userEntity.getPassword())) {
            throw new SimpleSnsException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성
        String token = JwtTokenUtils.generateToken(username, secretKey, expiredTimeMs);

        return token;
    }

    public Page<Alarm> alarmList(String username, Pageable pageable) {
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() -> new SimpleSnsException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        return alarmEntityRepository.findAllByUser(userEntity, pageable).map(Alarm::fromEntity);
    }
}
