package com.novelbot.api.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String userName;
    private String userNickname;
    private String userEmail;
}