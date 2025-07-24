package com.novelbot.api.dto.reading;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadingProgressResponse {
    private Long episodeId;
    private Long novelId;
    private Long userId;
    private int lastReadPage;
    private LocalDateTime updatedAt;
}
