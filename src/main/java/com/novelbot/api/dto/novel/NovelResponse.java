package com.novelbot.api.dto.novel;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NovelResponse {
    private Long novelId;
    private String title;
    private String author;
    private String description;
    private String genre;
    private String coverImageUrl;
}
