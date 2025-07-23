package com.novelbot.api.controller;

import com.novelbot.api.utility.ImportNovel.NovelImport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@EnableScheduling
public class NovelController {
    @Autowired
    private NovelImport novelimport;

    @Scheduled(fixedRate = 10000)
    public void  readNovelList() {
        try {
            novelimport.importFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
