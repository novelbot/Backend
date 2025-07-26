package com.novelbot.api.dto.novel;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class EpisodeListDto {
    private int episodeId;
    private int novelId;
    private int episodeNumber;
    private String episodeTitle;
    private String publicationDate;
}
