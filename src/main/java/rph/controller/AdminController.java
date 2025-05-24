package rph.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @PostMapping("/item")
    public ResponseEntity<String> addItem() {
        // 아이템 추가 구현
        return ResponseEntity.ok("아이템 추가 로직 위치");
    }


}
