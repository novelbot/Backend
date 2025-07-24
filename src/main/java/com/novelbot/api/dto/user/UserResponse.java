package com.novelbot.api.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String userName;
    private String userNickname;
    private String userEmail;
    private String createdAt;
}
