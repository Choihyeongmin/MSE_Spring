package rph.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rph.dto.game.GameResultRequest;
import rph.service.game.GameService;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping
    public ResponseEntity<?> saveGameResult(@RequestBody GameResultRequest request) {
        gameService.saveGameResult(request);
        return ResponseEntity.ok().build();
    }
}
