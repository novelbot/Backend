package com.novelbot.api.controller;

import com.novelbot.api.domain.Novel;
import com.novelbot.api.service.settingNovel.NovelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
@EnableScheduling
public class NovelController {
    @Autowired
    private NovelService novelService;

    @GetMapping("/novels")
    public List<Novel> getAllNovels(){
        return novelService.findAllNovels();
    }

    @Scheduled(fixedRate = 10000)
    public void  readNovelList() {
        try {
            novelimport.importFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
