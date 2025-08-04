package com.novelbot.api.mapper.join;

import com.novelbot.api.domain.User;
import com.novelbot.api.dto.join.UserCreateRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Component;

@Component
public class UserCreateRequestMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User toUser(UserCreateRequest userCreateRequest) {
        return User.create(userCreateRequest, passwordEncoder);
    }
}
