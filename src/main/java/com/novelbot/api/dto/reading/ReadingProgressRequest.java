package com.novelbot.api.dto.reading;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadingProgressRequest {
    private Integer novelId;
    private Integer episodeId;
    private Integer lastReadPage;
}
