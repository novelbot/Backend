package com.novelbot.api.mapper.novel;

import com.novelbot.api.domain.Novel;
import com.novelbot.api.dto.novel.NovelDto;
import org.springframework.stereotype.Component;

@Component
public class NovelDtoMapper {
    public NovelDto toDto(Novel novel){
        NovelDto novelDto = new NovelDto();

        novelDto.setNovelId(novel.getId());
        novelDto.setTitle(novel.getTitle());
        novelDto.setAuthor(novel.getAuthor());
        novelDto.setDescription(novel.getDescription());
        novelDto.setGenre(novel.getGenre());
        novelDto.setCoverImageUrl(novel.getCoverImageUrl());

        return novelDto;
    }
}
