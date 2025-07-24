package com.novelbot.api.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginAuthDTO {
    private String username;
    private String password;
}
