package com.novelbot.api.controller;

import com.novelbot.api.dto.NovelDTO;
import com.novelbot.api.service.NovelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/novels")
public class NovelController {
    private final NovelService novelService;

    public NovelController(NovelService novelService) {
        this.novelService = novelService;
    }

    @GetMapping
    public List<NovelDTO> getNovels() {
        return novelService.getNovelList();
    }
}
