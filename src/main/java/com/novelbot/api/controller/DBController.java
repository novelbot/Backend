package com.novelbot.api.controller;

import com.novelbot.api.utility.ImportNovel.NovelImport;
import com.novelbot.api.utility.ImportEpisode.EpisodeImport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@EnableScheduling
public class DBController {
    @Autowired
    private NovelImport novelImport;

    @Autowired
    private EpisodeImport episodeImport;

    @Scheduled(fixedRate = 30000)
    public void importData() {
        try {
            // 1. Novel 데이터 임포트
            System.out.println("소설 데이터 임포트 시작...");
            novelImport.importFile();
            System.out.println("소설 데이터 임포트 완료.");

            // 2. Episode 데이터 임포트
            System.out.println("에피소드 데이터 임포트 시작...");
            episodeImport.importFile();
            System.out.println("에피소드 데이터 임포트 완료.");
        } catch (IOException e) {
            System.err.println("데이터 임포트 실패: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}