import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/reading-progress")
public class ReadingProgressController {

    /**
     * 새로운 독서 진도 저장
     */
    @PostMapping
    public ResponseEntity<Void> saveReadingProgress(@RequestBody ReadingProgressRequest request) {
        // TODO: 독서 진도 저장 로직
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 기존 독서 진도 업데이트
     */
    @PatchMapping
    public ResponseEntity<Void> updateReadingProgress(@RequestBody ReadingProgressRequest request) {
        // TODO: 독서 진도 업데이트 로직
        return ResponseEntity.ok().build();
    }

    /**
     * 읽던 페이지로 이동
     * API 명세서의 302 Found 응답을 따릅니다.
     */
    @GetMapping
    public ResponseEntity<ReadingProgressDto> getReadingProgress() {
        // TODO: 마지막 읽은 위치 정보 조회 로직
        // ReadingProgressResponse response = readingProgressService.getLastReadProgress(userId);
        // return ResponseEntity.status(HttpStatus.FOUND).body(response);
        return ResponseEntity.status(HttpStatus.FOUND).build(); // 임시 반환
    }
}