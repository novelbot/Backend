package com.novelbot.api.mapper.user;
import com.novelbot.api.domain.User;
import com.novelbot.api.dto.user.UserCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class UserCreateRequestDtoMapper {
    public UserCreateRequest toUserDto(User user) {
        UserCreateRequest userDto = new UserCreateRequest();

        userDto.setUserName(user.getUserName());
        if(user.getUserNickname() != null){
            userDto.setUserNickname(user.getUserNickname());
        }
        userDto.setUserEmail(user.getUserEmail());
        userDto.setUserPassword(user.getUserPassword());

        return userDto;
    }
}
