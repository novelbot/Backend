package com.novelbot.api.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    private String userName;
    private String userNickname;
    private String userEmail;
    private String userPassword;
}
