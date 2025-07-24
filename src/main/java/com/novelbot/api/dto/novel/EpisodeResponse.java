package com.novelbot.api.dto.novel;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeResponse {
    private Long episodeId;
    private Long novelId;
    private String episodeTitle;
    private int episodeNumber;
    private String content;
    private String publicationDate;
}
