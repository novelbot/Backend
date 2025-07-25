import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChatController {

    // Chatroom
    @PostMapping("/chatrooms")
    public ResponseEntity<Void> createChatroom(@RequestBody ChatroomCreateRequest request) {
        // TODO: 채팅방 생성 로직
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/chatrooms/{chatId}")
    public ResponseEntity<Void> deleteChatroom(@PathVariable Integer chatId) {
        // TODO: 채팅방 삭제 로직
        return ResponseEntity.noContent().build();
    }

    // Queries
    @PostMapping("/chatrooms/{chatId}/queries")
    public ResponseEntity<Void> createQuery(@PathVariable Integer chatId, @RequestBody QueryCreateRequest request) {
        // TODO: 질문 생성 로직
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/chatrooms/{chatId}/queries")
    public ResponseEntity<List<Query>> getQueries(@PathVariable Integer chatId) {
        // TODO: 질문 목록 조회 로직
        return ResponseEntity.ok().build();
    }

    @GetMapping("/chatrooms/{chatId}/queries/{queryId}")
    public ResponseEntity<Query> getQuery(@PathVariable Integer chatId, @PathVariable Integer queryId) {
        // TODO: 질문 내용 조회 로직
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/queries/{queryId}")
    public ResponseEntity<Void> deleteQuery(@PathVariable Integer queryId) {
        // TODO: 쿼리 삭제 로직
        return ResponseEntity.noContent().build();
    }

    // Answer
    @GetMapping("/queries/{queryId}/answer")
    public ResponseEntity<AnswerResponse> getAnswer(@PathVariable Integer queryId) {
        // TODO: 답변 생성 로직
        return ResponseEntity.ok().build();
    }
}