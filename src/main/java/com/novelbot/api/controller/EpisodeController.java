import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/novels/{novelId}/episodes")
public class EpisodeController {
  
    @GetMapping
    public ResponseEntity<List<EpisodeDto>> getEpisodes(@PathVariable Integer novelId) {
        // TODO: 특정 소설의 에피소드 목록 조회 로직
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> registerEpisode(@PathVariable Integer novelId, @RequestBody EpisodeCreateRequest request) {
        // TODO: 에피소드 등록 로직
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{episodeNumber}")
    public ResponseEntity<EpisodeDto> getEpisodeContent(@PathVariable Integer novelId, @PathVariable Integer episodeNumber) {
        // TODO: 웹소설 본문 조회 로직
        return ResponseEntity.ok().build();
    }
}