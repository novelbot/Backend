package com.novelbot.api.controller;

import com.novelbot.api.utility.ImportNovel.NovelImport;
import com.novelbot.api.utility.ImportEpisode.EpisodeImport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping; // GetMapping 임포트

// @RestController
public class DBController {

    @Autowired
    private NovelImport novelImport;

    @Autowired
    private EpisodeImport episodeImport;

    // @Scheduled(fixedRate = 30000) // 스케줄링은 주석 처리해 둡니다.

    // 브라우저에서 이 주소를 호출하면 아래 코드가 실행됩니다.
    @GetMapping("/manual-import")
    public String importData() {
        try {
            System.out.println("수동 데이터 임포트 시작...");

            // 1. 소설 데이터 임포트
            novelImport.importFile();
            System.out.println("소설 데이터 임포트 완료.");

            // 2. 에피소드 데이터 임포트
            episodeImport.importFile();
            System.out.println("에피소드 데이터 임포트 완료.");

            return "<h1>데이터 임포트 성공!</h1><p>DB를 확인해 보세요.</p>";
        } catch (Exception e) {
            System.err.println("데이터 임포트 실패: " + e.getMessage());
            e.printStackTrace(); // 터미널에 에러 로그를 자세히 출력합니다.
            // 브라우저에도 에러 메시지를 보여줍니다.
            return "<h1>데이터 임포트 실패</h1><p>에러: " + e.getMessage() + "</p><p>서버 로그를 확인해 주세요.</p>";
        }
    }
}