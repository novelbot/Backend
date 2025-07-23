package com.novelbot.api.utility.ImportEpisode;

import com.novelbot.api.domain.Episode;
import com.novelbot.api.domain.Novel;
import com.novelbot.api.repository.EpisodeRepository;
import com.novelbot.api.repository.NovelRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void importFile() throws IOException {
        try {
            String filePath = "src/main/resources/EpisodeFile.xlsx";
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("엑셀 파일이 존재하지 않습니다: " + filePath);
            }

            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            List<Episode> episodes = new ArrayList<>();
            int rowNum = 1; // 헤더 제외한 행 번호
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                rowNum++;
                if (row.getCell(0) == null) {
                    continue;
                }

                Episode episode = new Episode();
                String idValue = getStringCellValue(row.getCell(1));
                if (idValue == null || idValue.isBlank()) {
                    System.err.println("행 " + rowNum + ": episode_id가 비어있음");
                    continue;
                }
                try {
                    episode.setEpisode_id(Long.parseLong(idValue));
                } catch (NumberFormatException e) {
                    System.err.println("행 " + rowNum + ": episode_id 형식이 잘못됨 - " + idValue);
                    continue;
                }

                try {
                    Cell novelIdCell = row.getCell(2);
                    String novelIdValue = getStringCellValue(novelIdCell);
                    if (novelIdValue == null || novelIdValue.isBlank()) {
                        System.err.println("행 " + rowNum + ": novel_id가 비어있음");
                        continue;
                    }
                    Long novelId = Long.parseLong(novelIdValue);
                    Novel novel = novelRepository.findById(novelId)
                            .orElseThrow(() -> new RuntimeException("소설을 찾을 수 없습니다. ID: " + novelId));
                    episode.setNovel(novel);
                } catch (Exception e) {
                    System.err.println("행 " + rowNum + ": novel_id 처리 실패 - " + e.getMessage());
                    continue;
                }

                episode.setEpisode_number((int) row.getCell(3).getNumericCellValue());
                episode.setEpisode_title(getStringCellValue(row.getCell(4)));
                episode.setContent(getStringCellValue(row.getCell(5)));
                episode.setPublication_date(new Date(row.getCell(6).getDateCellValue().getTime()));
                episodes.add(episode);
            }

            episodeRepository.saveAll(episodes);

            workbook.close();
            fis.close();

            System.out.println("에피소드 데이터가 성공적으로 DB에 저장되었습니다.");
        } catch(IOException e){
            System.out.println("에피소드 데이터 저장 실패: " + e.getMessage());
        }
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            default -> null;
        };
    }
}