package com.novelbot.api.controller;

import com.novelbot.api.service.settingEpisode.EpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class EpisodeController {
    @Autowired
    private EpisodeService episodeService;

    @PostMapping("/upload/episodes")
    public String uploadEpisodes(@RequestParam("src/main/resources/EpisodeFile") MultipartFile file) {
        try {
            episodeService.importFile(file);
            return "Episodes imported successfully!";
        } catch (IOException e) {
            return "Error importing episodes: " + e.getMessage();
        }
    }
}
