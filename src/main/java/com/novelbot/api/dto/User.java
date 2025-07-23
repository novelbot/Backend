package com.novelbot.api.dto;

import lombok.Data;

@Data
public class User {
    private Long userId;
    private String userPassword;
    private String userName;
    private String userNickname;
    private String userEmail;
    private String createdAt;
}
