package com.novelbot.api.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Query_Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long query_id;

    private Long chat_id;

    private Long user_id;

    private Long novel_id;

    private int episode_number;
}
