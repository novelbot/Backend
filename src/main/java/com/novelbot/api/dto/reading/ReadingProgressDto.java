package com.novelbot.api.dto.reading;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadingProgressDto {
    private Integer userId;
    private Integer novelId;
    private Integer episodeId;
    private Integer lastReadPage;
    private LocalDateTime updatedAt;
}
