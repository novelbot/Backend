package com.novelbot.api.mapper.auth;

import com.novelbot.api.domain.User;
import com.novelbot.api.dto.auth.LoginRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginRequestDtoMapper {
    public LoginRequest toDto(Optional<User> user){
        LoginRequest loginRequestDto = new LoginRequest();

        if(user.isPresent()){
            loginRequestDto.setUsername(user.get().getUserName());
            loginRequestDto.setPassword(user.get().getUserPassword());
            return loginRequestDto;
        }
        return null;
    }
}
