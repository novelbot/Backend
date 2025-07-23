package com.novelbot.api.controller;

import com.novelbot.api.domain.Episode;
import com.novelbot.api.service.settingEpisode.EpisodeService;
import com.novelbot.api.utility.ImportEpisode.EpisodeImport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
@EnableScheduling
@RestController
@RequestMapping("/api")
public class EpisodeController {
    @Autowired
    private EpisodeService episodeService;

    @Autowired
    private EpisodeImport episodeimport;

//    @GetMapping("/novels/{novel_Id}/episodes")
//    public List<Episode> getEpisodesByNovelId(@PathVariable Long novel_Id) throws IOException {
//        return episodeService.findEpisodesByNovelId(novel_Id);
//    }

    // @PostMapping("/upload/episodes")
    // public String uploadEpisodes(@RequestParam("file") MultipartFile file) {
    //     try {
    //         episodeimport.importFile();
    //     } catch (IOException e) {
    //         throw new RuntimeException(e);
    //     }
    // }

}
