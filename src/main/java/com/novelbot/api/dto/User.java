package com.novelbot.api.dto;

import lombok.Data;

@Data
public class User {
    private Long user_id;
    private Long user_password;
    private String user_name;
    private String user_nickname;
    private String user_email;
    private String created_at;
}
