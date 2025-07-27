package com.novelbot.api.mapper.reading;

import com.novelbot.api.domain.UserReadingProgress;
import com.novelbot.api.dto.reading.ReadingProgressDto;
import org.springframework.stereotype.Component;

@Component
public class ReadingProgressDtoMapper {
    ReadingProgressDto toDto(UserReadingProgress userReadingProgress){
        ReadingProgressDto readingProgressDto = new ReadingProgressDto();

        readingProgressDto.setNovelId(userReadingProgress.getNovel().getNovelId());
        readingProgressDto.setUserId(userReadingProgress.getUser().getUserId());
        readingProgressDto.setEpisodeId(userReadingProgress.getEpisode().getEpisodeId());
        readingProgressDto.setLastReadPage(userReadingProgress.getLastReadPage());
        readingProgressDto.setUpdatedAt(userReadingProgress.getUpdatedAt().toLocalDateTime());

        return readingProgressDto;
    }
}
