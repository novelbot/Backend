package com.novelbot.service.settingEpisode;

import com.novelbot.entity.Episode;
import com.novelbot.entity.Novel;
import com.novelbot.repository.EpisodeRepository;
import com.novelbot.repository.NovelRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class EpisodeService {
    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private NovelRepository novelRepository;

    public void importFile(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 사용

        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        List<Episode> episodes = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getCell(0) == null) {
                continue;
            }

            // Episode 객체 생성
            Episode episode = new Episode();

            episode.setEpisode_id(Long.valueOf(getStringCellValue(row.getCell(1))));

            // Novel 관계 설정: novel_id를 통해 Novel 객체 조회
            Long novelId = (long) row.getCell(2).getNumericCellValue(); // novel_id 열 가정
            Novel novel = novelRepository.findById(novelId)
                    .orElseThrow(() -> new RuntimeException("Novel not found with ID: " + novelId));
            episode.setNovel(novel);

            episodes.add(episode);

            episode.setEpisode_number((int) row.getCell(3).getNumericCellValue());
            episode.setEpisode_title(getStringCellValue(row.getCell(4)));
            episode.setContent(getStringCellValue(row.getCell(5)));
            episode.setPublication_date(new Date(row.getCell(6).getDateCellValue().getTime()));
        }

        episodeRepository.saveAll(episodes);

        workbook.close();
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