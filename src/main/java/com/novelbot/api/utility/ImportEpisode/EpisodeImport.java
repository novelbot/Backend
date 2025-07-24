package com.novelbot.api.utility.ImportEpisode;

import com.novelbot.api.domain.Episode;
import com.novelbot.api.domain.Novel;
import com.novelbot.api.repository.EpisodeRepository;
import com.novelbot.api.repository.NovelRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class EpisodeImport {
    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private NovelRepository novelRepository;

    @Transactional
    public void importFile() throws IOException {
        try {
            String filePath = "src/main/resources/EpisodeFile.xlsx";
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("엑셀 파일이 존재하지 않습니다: " + filePath);
                return;
            }

            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                rowIterator.next(); // 헤더 행 건너뛰기
            }

            List<Episode> episodes = new ArrayList<>();
            int rowNum = 1;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                rowNum++;
//                if (row.getCell(0) == null) {
//                    System.err.println("행 " + rowNum + ": 첫 번째 셀이 비어 있습니다.");
//                    continue;
//                }

                Episode episode = new Episode();

                // 소설 제목 처리
                try {
                    Cell novelTitleCell = row.getCell(2);
                    String novelTitle = getStringCellValue(novelTitleCell);
                    if (novelTitle == null || novelTitle.isBlank()) {
                        System.err.println("행 " + rowNum + ": 소설 제목이 비어 있습니다.");
                        break;
                    }
                    novelTitle = novelTitle.trim();
                    String finalNovelTitle = novelTitle;
                    Novel novel = novelRepository.findByTitle(novelTitle)
                            .orElseThrow(() -> new RuntimeException("소설을 찾을 수 없습니다. 제목: " + finalNovelTitle));
                    episode.setNovel(novel);
                } catch (Exception e) {
                    System.err.println("행 " + rowNum + ": 소설 제목 처리 실패 - " + e.getMessage());
                    break;
                }

                // 에피소드 번호 처리
                try {
                    Cell episodeNumberCell = row.getCell(3);
                    String episodeNumberStr = getStringCellValue(episodeNumberCell);
                    if (episodeNumberStr == null || episodeNumberStr.trim().isEmpty()) {
                        System.err.println("행 " + rowNum + ": 에피소드 번호가 비어 있습니다.");
                        break;
                    }
                    episode.setEpisodeNumber(Integer.parseInt(episodeNumberStr));
                } catch (NumberFormatException e) {
                    System.err.println("행 " + rowNum + ": 에피소드 번호 형식이 잘못되었습니다 - " + e.getMessage());
                    break;
                }

                episode.setEpisodeTitle(getStringCellValue(row.getCell(4)));
                episode.setContent(getStringCellValue(row.getCell(5)));
                try {
                    episode.setPublicationDate(new Date(row.getCell(6).getDateCellValue().getTime()));
                } catch (Exception e) {
                    System.err.println("행 " + rowNum + ": 출간일 처리 실패 - " + e.getMessage());
                    break;
                }

                episodes.add(episode);
            }

            System.out.println("저장할 에피소드 수: " + episodes.size()); // 디버깅용
            episodeRepository.saveAll(episodes);

            workbook.close();
            fis.close();

            System.out.println("에피소드 데이터가 성공적으로 DB에 저장되었습니다.");
        } catch (IOException e) {
            System.out.println("에피소드 데이터 저장 실패: " + e.getMessage());
            throw e;
        }
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            default -> null;
        };
    }
}