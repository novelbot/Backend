package com.novelbot.api.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private String userName;
    private String userPassword;
    private String userNickname;
    private String userEmail;
}