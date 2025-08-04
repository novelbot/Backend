package com.novelbot.api.dto.join;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateRequest {
    private String userName;
    private String userNickname;
    private String userEmail;
    private String userPassword;
}
