package com.novelbot.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    private Long user_password;

    private String user_name;

    private String user_nickname;

    private String user_email;

    private String created_at;

}
