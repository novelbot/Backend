package com.novelbot.api.controller;

import com.novelbot.api.dto.novel.EpisodeListDto;
import com.novelbot.api.service.novel.EpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.novel.EpisodeCreateRequest;
import com.novelbot.api.dto.novel.EpisodeDto;

import java.util.List;

@RestController
@RequestMapping("/novels/{novelId}/episodes")
public class EpisodeController {

    @Autowired
    private EpisodeService episodeFetchService;

    @GetMapping
    public ResponseEntity<List<EpisodeListDto>> getEpisodes(@PathVariable Integer novelId) {
        // TODO: 특정 소설의 에피소드 목록 조회 로직
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> registerEpisode(@PathVariable Integer novelId, @RequestBody EpisodeCreateRequest request) {
        // TODO: 에피소드 등록 로직
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{episodeNumber}")
    public ResponseEntity<EpisodeDto> getEpisodeContent(@PathVariable Integer novelId, @PathVariable Integer episodeNumber) {
        // TODO: 웹소설 본문 조회 로직
        return ResponseEntity.ok().build();
    }
}