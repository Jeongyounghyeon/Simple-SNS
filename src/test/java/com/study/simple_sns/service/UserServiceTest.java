package com.study.simple_sns.service;

import com.study.simple_sns.exception.ErrorCode;
import com.study.simple_sns.exception.SimpleSnsException;
import com.study.simple_sns.filxture.UserEntityFixture;
import com.study.simple_sns.model.entity.UserEntity;
import com.study.simple_sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserEntityRepository userEntityRepository;

    @MockitoBean
    private BCryptPasswordEncoder encoder;

    @Test
    void 회원가입이_정상적으로_동작하는_경우() {
        String username = "username";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(username, password, 1);

        // mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(fixture);

        Assertions.assertDoesNotThrow(() -> userService.join(username, password));
    }

    @Test
    void 회원가입시_username으로_회원가입한_유저가_이미_있는경우() {
        String username = "username";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(username, password, 1);

        // mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(fixture));
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        SimpleSnsException e = Assertions.assertThrows(SimpleSnsException.class, () -> userService.join(username, password));
        assertEquals(ErrorCode.DUPLICATED_USER_NAME, e.getErrorCode());
    }

    @Test
    void 로그인이_정상적으로_동작하는_경우() {
        String username = "username";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(username, password, 1);

        // mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(fixture));
        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> userService.login(username, password));
    }

    @Test
    void 로그인시_username으로_회원가입한_유저가_없는_경우() {
        String username = "username";
        String password = "password";

        // mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());

        SimpleSnsException e = Assertions.assertThrows(SimpleSnsException.class, () -> userService.login(username, password));
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 로그인시_패스워드가_틀린_경우() {
        String username = "username";
        String password = "password";
        String wrongPassword = "wrongPassword";

        UserEntity fixture = UserEntityFixture.get(username, password, 1);

        // mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(fixture));

        SimpleSnsException e = Assertions.assertThrows(SimpleSnsException.class, () -> userService.login(username, wrongPassword));
        assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
    }
}
