package com.novelbot.api.utility;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    
    private static final String DB_URL = "jdbc:mysql://35.216.85.254:3306/novelbot";
    private static final String DB_USER = "jungjin";
    private static final String DB_PASSWORD = "092166";
    
    public static void main(String[] args) {
        DataLoader loader = new DataLoader();
        try {
            // Excel 파일에서 데이터 로드
            List<Novel> novels = loader.loadNovelsFromExcel("src/test/resources/Novel.xlsx");
            List<Episode> episodes = loader.loadEpisodesFromExcel("src/test/resources/Episode.xlsx");
            
            // 데이터베이스에 삽입
            loader.insertDataToDatabase(novels, episodes);
            
            System.out.println("데이터 삽입이 완료되었습니다.");
            
        } catch (Exception e) {
            System.err.println("데이터 로드 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public List<Novel> loadNovelsFromExcel(String filePath) throws IOException {
        List<Novel> novels = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            
            // 첫 번째 행은 헤더로 건너뛰기
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                Novel novel = new Novel();
                
                // Excel 컬럼 순서: novel_id(0), title(1), author(2), description(3), genre(4), cover_image_url(5)
                Cell titleCell = row.getCell(1);
                if (titleCell != null) {
                    novel.title = getCellStringValue(titleCell);
                }
                
                Cell authorCell = row.getCell(2);
                if (authorCell != null) {
                    novel.author = getCellStringValue(authorCell);
                }
                
                Cell descriptionCell = row.getCell(3);
                if (descriptionCell != null) {
                    novel.description = getCellStringValue(descriptionCell);
                }
                
                Cell genreCell = row.getCell(4);
                if (genreCell != null) {
                    novel.genre = getCellStringValue(genreCell);
                }
                
                Cell coverImageCell = row.getCell(5);
                if (coverImageCell != null) {
                    novel.coverImageUrl = getCellStringValue(coverImageCell);
                }
                
                novels.add(novel);
            }
        }
        
        System.out.println("Novel.xlsx에서 " + novels.size() + "개의 소설 데이터를 로드했습니다.");
        return novels;
    }
    
    public List<Episode> loadEpisodesFromExcel(String filePath) throws IOException {
        List<Episode> episodes = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            
            // 첫 번째 행은 헤더로 건너뛰기
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                Episode episode = new Episode();
                
                // Excel 컬럼 순서: episode_id(0), novel_id(1), episode_num(2), episode_title(3), content(4), publication_date(5)
                Cell novelIdCell = row.getCell(1);
                if (novelIdCell != null) {
                    episode.novelId = (int) getCellNumericValue(novelIdCell);
                }
                
                Cell episodeNumberCell = row.getCell(2);
                if (episodeNumberCell != null) {
                    episode.episodeNumber = (int) getCellNumericValue(episodeNumberCell);
                }
                
                Cell episodeTitleCell = row.getCell(3);
                if (episodeTitleCell != null) {
                    episode.episodeTitle = getCellStringValue(episodeTitleCell);
                }
                
                Cell contentCell = row.getCell(4);
                if (contentCell != null) {
                    episode.content = getCellStringValue(contentCell);
                }
                
                Cell publicationDateCell = row.getCell(5);
                if (publicationDateCell != null && publicationDateCell.getCellType() == CellType.NUMERIC) {
                    if (DateUtil.isCellDateFormatted(publicationDateCell)) {
                        episode.publicationDate = new java.sql.Date(publicationDateCell.getDateCellValue().getTime());
                    }
                }
                
                episodes.add(episode);
            }
        }
        
        System.out.println("Episode.xlsx에서 " + episodes.size() + "개의 에피소드 데이터를 로드했습니다.");
        return episodes;
    }
    
    private String getCellStringValue(Cell cell) {
        if (cell == null) return null;
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
    
    private double getCellNumericValue(Cell cell) {
        if (cell == null) return 0;
        
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return 0;
                }
            default:
                return 0;
        }
    }
    
    public void insertDataToDatabase(List<Novel> novels, List<Episode> episodes) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);
            
            try {
                // 소설 데이터 삽입
                insertNovels(conn, novels);
                
                // 에피소드 데이터 삽입 (이미 novel_id가 있음)
                insertEpisodes(conn, episodes);
                
                conn.commit();
                System.out.println("모든 데이터가 성공적으로 삽입되었습니다.");
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
    
    private void insertNovels(Connection conn, List<Novel> novels) throws SQLException {
        String insertSQL = "INSERT INTO novels (title, author, description, genre, cover_image_url) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            for (Novel novel : novels) {
                pstmt.setString(1, novel.title);
                pstmt.setString(2, novel.author);
                pstmt.setString(3, novel.description);
                pstmt.setString(4, novel.genre);
                pstmt.setString(5, novel.coverImageUrl);
                
                pstmt.executeUpdate();
                System.out.println("소설 삽입 완료: " + novel.title);
            }
        }
    }
    
    private void insertEpisodes(Connection conn, List<Episode> episodes) throws SQLException {
        String insertSQL = "INSERT INTO episode (episode_title, episode_number, content, publication_date, novel_id) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            for (Episode episode : episodes) {
                pstmt.setString(1, episode.episodeTitle);
                pstmt.setInt(2, episode.episodeNumber);
                pstmt.setString(3, episode.content);
                
                if (episode.publicationDate != null) {
                    pstmt.setDate(4, episode.publicationDate);
                } else {
                    pstmt.setNull(4, Types.DATE);
                }
                
                pstmt.setInt(5, episode.novelId);
                
                pstmt.executeUpdate();
                System.out.println("에피소드 삽입 완료: " + episode.episodeTitle + " (소설 ID: " + episode.novelId + ")");
            }
        }
    }
    
    // 데이터 클래스들
    public static class Novel {
        public String title;
        public String author;
        public String description;
        public String genre;
        public String coverImageUrl;
    }
    
    public static class Episode {
        public String episodeTitle;
        public int episodeNumber;
        public String content;
        public java.sql.Date publicationDate;
        public int novelId; // Excel의 novel_id 컬럼값
    }
}