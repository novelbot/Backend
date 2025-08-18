package com.novelbot.api.dto.novel;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class EpisodeListDto {
    private Integer episodeId;
    private Integer novelId;
    private Integer episodeNumber;
    private String episodeTitle;
    private String publicationDate;
    private Boolean isPurchased;
}
