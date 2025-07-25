package com.novelbot.api.controller;

import com.novelbot.api.domain.Novel;
import com.novelbot.api.service.searchingNovel.NovelSearchService;
import com.novelbot.api.service.settingNovel.NovelService;
import com.novelbot.api.utility.ImportNovel.NovelImport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public class NovelController {
    @Autowired
    private NovelService novelService;

    @Autowired
    private NovelSearchService novelSearchService;

    @Autowired
    private NovelImport novelimport;

    @GetMapping("/novels")
    public List<Novel> getAllNovels(){
        return novelService.findAllNovels();
    }

    @GetMapping("/novels/search")
    public ResponseEntity<Novel> searchNovel(@RequestParam("title") String title){
        Novel novel = novelSearchService.searchNovel(title);
        return ResponseEntity.ok(novel);
    }
}
