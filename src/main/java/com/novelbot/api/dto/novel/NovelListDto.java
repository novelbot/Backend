package com.novelbot.api.dto.novel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NovelListDto {
    private Integer novelId;
    private String title;

    public NovelListDto(Integer novelId, String title) {
        this.novelId = novelId;
        this.title = title;
    }
}