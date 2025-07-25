package com.novelbot.api.dto.novel;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class EpisodeCreateRequest {
    private int episodeNumber;
    private String episodeTitle;
    private String content;
    private LocalDate publicationDate;
}
