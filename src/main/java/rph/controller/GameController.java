package rph.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rph.dto.game.GameResultRequest;
import rph.dto.game.GameResultResponse;
import rph.dto.game.relativeRecodeResponse;
import rph.service.game.GameService;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    @Autowired
    private final GameService gameService; // Service to handle game logic

    @PostMapping
    public ResponseEntity<GameResultResponse> saveGameResult(@Valid @RequestBody GameResultRequest request) {
        // Save game result
        GameResultResponse response = gameService.saveGameResult(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{playerid}")
    public List<GameResultResponse> getGameResult(@PathVariable Long playerid) {
        // Get all game results for a user
        return gameService.findByPlayerId(playerid);
    }

    @GetMapping("/recode/{user1id}/{user2id}")
    public relativeRecodeResponse getRelativeRecode(@PathVariable Long user1id, @PathVariable Long user2id) {
        // Get head-to-head record between two users
        return gameService.findRelativeRecode(user1id, user2id);
    }
}