package com.novelbot.api.dto.novel;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NovelCreateRequest {
    private String title;
    private String author;
    private String description;
    private String genre;
    private String coverImageUrl;
}
