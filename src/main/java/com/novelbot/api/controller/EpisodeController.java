package com.novelbot.api.controller;

import com.novelbot.api.dto.novel.EpisodeListDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.novel.EpisodeDto;
import com.novelbot.api.service.novel.EpisodeService;

import io.swagger.v3.oas.annotations.Operation;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/novels/{novelId}/episodes")
public class EpisodeController {

    private final EpisodeService episodeService;

    public EpisodeController(EpisodeService episodeService) {
        this.episodeService = episodeService;
    }

    @Operation(summary = "에피소드 목록 조회", description = "특정 웹소설의 에피소드 목록을 조회하는 API")
    @GetMapping
    public ResponseEntity<List<EpisodeListDto>> getEpisodes(@PathVariable Integer novelId) throws IOException {
        List<EpisodeListDto> episodes = episodeService.findAllByNovelId(novelId);
        return ResponseEntity.ok(episodes);
    }

    // @PostMapping
    // public ResponseEntity<Void> registerEpisode(@PathVariable Integer novelId,
    // @RequestBody EpisodeCreateRequest request) {
    // episodeService.createEpisode(novelId, request);
    // return ResponseEntity.status(HttpStatus.CREATED).build();
    // }

    @Operation(summary = "에피소드 내용 조회", description = "특정 에피소드의 내용을 조회하는 API")
    @GetMapping("/{episodeNumber}")
    public ResponseEntity<EpisodeDto> getEpisodeContent(
            @PathVariable Integer novelId,
            @PathVariable Integer episodeNumber) throws IOException {

        Optional<EpisodeDto> episodeDto = episodeService.getEpisodeContent(novelId, episodeNumber);

        return episodeDto
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}