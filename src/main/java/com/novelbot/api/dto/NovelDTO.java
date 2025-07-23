package com.novelbot.api.dto;

import lombok.*;

@Data
public class NovelDTO {
    private Long novelId;
    private String title;
    private String author;
    private String description;
    private String genre;
    private String coverImageUrl;
}
