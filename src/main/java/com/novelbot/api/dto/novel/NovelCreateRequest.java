package com.novelbot.api.dto.novel;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class NovelCreateRequest {
    private String title;
    private String author;
    private String description;
    private String genre;
    private String coverImageUrl;
}
