package com.novelbot.api.dto;

import com.novelbot.api.domain.Novel;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NovelDTO {
    private Long novelId;
    private String title;
    private String author;
    private String description;
    private String genre;
    private String coverImageUrl;

    public static NovelDTO fromEntity(Novel novel) {
        return new NovelDTO(
                novel.getId(),
                novel.getTitle(),
                novel.getAuthor(),
                novel.getDescription(),
                novel.getGenre(),
                novel.getCoverImageUrl()
        );
    }
}

