package com.novelbot.api.dto.reading;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadingProgressRequest {
    private Long episodeId;
    private Long novelId;
    private Long userId;
    private int lastReadPage;
}
