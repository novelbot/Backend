import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/novels")
public class NovelController {

    @GetMapping
    public ResponseEntity<List<Novel>> getNovelList() {
        // TODO: 소설 목록 조회 로직
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> registerNovel(@RequestBody Novel novel) {
        // TODO: 소설 등록 로직
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Novel>> searchNovels(@RequestParam String title) {
        // TODO: 소설 제목으로 검색하는 로직
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{novelId}")
    public ResponseEntity<Void> deleteNovel(@PathVariable Integer novelId) {
        // TODO: 소설 삭제 로직
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{novelId}")
    public ResponseEntity<Void> updateNovel(@PathVariable Integer novelId, @RequestBody Novel novel) {
        // TODO: 소설 수정 로직
        return ResponseEntity.ok().build();
    }
}