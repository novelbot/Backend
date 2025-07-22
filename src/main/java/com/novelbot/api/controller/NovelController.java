
import com.novelbot.api.domain.Novel;
import com.novelbot.api.dto.NovelDTO;
import com.novelbot.api.service.novel.NovelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/novels")
public class NovelController {

    private final NovelService novelService;

    public NovelController(NovelService novelService) {
        this.novelService = novelService;
    }

    @GetMapping
    public List<NovelDTO> getNovels() {
        List<Novel> novels = novelService.getNovelList();

        return novels.stream()
                .map(NovelDTO::fromEntity)
                .collect(Collectors.toList());
    }
}