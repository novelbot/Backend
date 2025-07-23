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

@RestController
@RequestMapping("/api/novels")
public class NovelController {

    @Autowired
    private NovelService novelService;

    @GetMapping("/novels")
    public List<Novel> getAllNovels(){
        return novelService.findAllNovels();
    }

    @PostMapping("/upload/novels")
    public String uploadNovels(@RequestParam("src/main/resources/NovelFile") MultipartFile file) {
        try {
            novelService.importFile(file);
            return "Novels imported successfully!";
        } catch (IOException e) {
            return "Error importing novels: " + e.getMessage();
        }
    }
}
