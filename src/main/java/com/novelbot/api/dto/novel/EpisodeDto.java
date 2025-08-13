package com.novelbot.api.dto.novel;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class EpisodeDto {
    private Integer novelId;
    private Integer episodeNumber;
    private String episodeTitle;
    private String content;
    private String publicationDate;
}
