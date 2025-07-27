package com.novelbot.api.controller;

import com.novelbot.api.dto.novel.EpisodeListDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.novel.EpisodeCreateRequest;
import com.novelbot.api.dto.novel.EpisodeDto;
import com.novelbot.api.service.novel.EpisodeService;

import java.util.List;

@RestController
@RequestMapping("/novels/{novelId}/episodes")
public class EpisodeController {

    private final EpisodeService episodeService;

    public EpisodeController(EpisodeService episodeService) {
        this.episodeService = episodeService;
    }

    @GetMapping
    public ResponseEntity<List<EpisodeListDto>> getEpisodes(@PathVariable Integer novelId) {
        List<EpisodeListDto> episodes = episodeService.getEpisodesByNovel(novelId);
        return ResponseEntity.ok(episodes);
    }

    @PostMapping
    public ResponseEntity<Void> registerEpisode(@PathVariable Integer novelId, @RequestBody EpisodeCreateRequest request) {
        episodeService.createEpisode(novelId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{episodeNumber}")
    public ResponseEntity<EpisodeDto> getEpisodeContent(@PathVariable Integer novelId, @PathVariable Integer episodeNumber) {
        EpisodeDto episodeDto = episodeService.getEpisodeContent(novelId, episodeNumber);
        return ResponseEntity.ok(episodeDto);
    }
}