package com.novelbot.api.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}
