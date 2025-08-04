package com.novelbot.api.mapper.join;

import com.novelbot.api.domain.User;
import com.novelbot.api.dto.join.UserDto;

import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {
    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setUserId(user.getId());
        userDto.setUserName(user.getUserName());
        if(user.getUserNickname() != null){
            userDto.setUserNickname(user.getUserNickname());
        }
        userDto.setUserEmail(user.getUserEmail());
        userDto.setCreatedAt(user.getCreatedAt().toString());

        return userDto;
    }
}
