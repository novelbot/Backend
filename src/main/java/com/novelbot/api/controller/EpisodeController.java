package com.novelbot.api.controller;

import com.novelbot.api.utility.ImportEpisode.EpisodeImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@EnableScheduling
public class EpisodeController {
    @Autowired
    private EpisodeImport episodeimport;

    @Scheduled(fixedRate = 10000)
    public void episodeNovelList() {
        try {
            episodeimport.importFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
