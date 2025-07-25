package com.novelbot.api.dto.reading;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadingProgressRequest {
    private int novelId;
    private int episodeId;
    private int lastReadPage;
}
