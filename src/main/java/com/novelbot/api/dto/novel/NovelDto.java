package com.novelbot.api.dto.novel;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class NovelDto {
    private Integer novelId;
    private String title;
    private String author;
    private String description;
    private String genre;
    private String coverImageUrl;
}
