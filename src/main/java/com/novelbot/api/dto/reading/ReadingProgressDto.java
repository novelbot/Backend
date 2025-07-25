package com.novelbot.api.dto.reading;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadingProgressDto {
    private int userId;
    private int novelId;
    private int episodeId;
    private int lastReadPage;
    private LocalDateTime updatedAt;
}
