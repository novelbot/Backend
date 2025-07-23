package com.novelbot.controller;

import com.novelbot.service.settingNovel.NovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class NovelController {
    @Autowired
    private NovelService novelService;

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
