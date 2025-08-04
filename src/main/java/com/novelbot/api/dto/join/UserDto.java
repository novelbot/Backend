package com.novelbot.api.dto.join;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer userId;
    private String userName;
    private String userNickname;
    private String userEmail;
    private String createdAt;
    private String userRole;
}
