package com.novelbot.api.dto;

import lombok.Data;

@Data
public class UserReadingProcessDTO {
    private Long episodeId;
    private Long novelId;
    private Long userId;
    private int lastReadPage;
    private String updatedAt;
}
