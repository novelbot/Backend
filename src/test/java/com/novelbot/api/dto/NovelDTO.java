package com.novelbot.api.dto;

import lombok.*;

@Data
public class NovelDTO {
    private Long novel_id;
    private String title;
    private String author;
    private String description;
    private String genre;
    private String cover_image_url;
}
