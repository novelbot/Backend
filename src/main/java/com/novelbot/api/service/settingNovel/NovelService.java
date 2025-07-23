package com.novelbot.api.service.settingNovel;

import com.novelbot.api.domain.Novel;
import com.novelbot.api.repository.NovelRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class NovelService {
    @Autowired
    private NovelRepository novelRepository;

    public void importFile(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 사용

        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        List<Novel> novels = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if(row.getCell(0) == null) {
                continue;
            }

            Novel novel = new Novel();
            novel.setNovel_id(Long.valueOf(getStringCellValue(row.getCell(1))));
            novel.setTitle(getStringCellValue(row.getCell(2)));
            novel.setAuthor(getStringCellValue(row.getCell(3)));
            novel.setDescription(getStringCellValue(row.getCell(4)));
            novel.setGenre(getStringCellValue(row.getCell(5)));
            novel.setCover_image_url(getStringCellValue(row.getCell(6)));
        }

        novelRepository.saveAll(novels);

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
