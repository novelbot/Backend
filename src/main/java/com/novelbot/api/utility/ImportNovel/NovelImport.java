package com.novelbot.api.utility.ImportNovel;

import com.novelbot.api.domain.Novel;
import com.novelbot.api.repository.NovelRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class NovelImport {
    @Autowired
    private NovelRepository novelRepository;

    public void importFile() throws IOException {
        try {
            String filePath = "src/main/resources/NovelFile.xlsx";
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
                rowIterator.next();
            }

            List<Novel> novels = new ArrayList<>();
            int rowNum = 1;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                rowNum++;
                if (row.getCell(0) == null) {
                    continue;
                }

                Novel novel = new Novel();

                String author = getStringCellValue(row.getCell(3));
                if (author == null || author.trim().isEmpty()) {
                    break;
                }

                novel.setTitle(getStringCellValue(row.getCell(2)));
                novel.setAuthor(author);
                novel.setDescription(getStringCellValue(row.getCell(4)));
                novel.setGenre(getStringCellValue(row.getCell(5)));
                novel.setCover_image_url(getStringCellValue(row.getCell(6)));

                novels.add(novel);
            }

            if (!novels.isEmpty()) {
                novelRepository.saveAll(novels);
                System.out.println("소설 데이터 " + novels.size() + "건이 성공적으로 DB에 저장되었습니다.");
            } else {
                System.out.println("삽입할 소설 데이터가 없습니다.");
            }

            workbook.close();
            fis.close();
        } catch (IOException e) {
            System.out.println("소설 데이터 저장 실패: " + e.getMessage());
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