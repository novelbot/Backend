package com.novelbot.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long user_id;
    private Long user_password;
    private String user_name;
    private String user_nickname;
    private String user_email;
    private String created_at;
}
