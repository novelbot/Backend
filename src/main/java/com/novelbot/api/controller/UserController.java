import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public ResponseEntity<Void> signup(@RequestBody UserCreate userCreate) {
        // TODO: 회원가입 비즈니스 로직 처리 (Service 호출)
        // 예: userService.createUser(userCreate);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        // TODO: 회원 탈퇴 비즈니스 로직 처리 (Service 호출)
        // 예: userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}