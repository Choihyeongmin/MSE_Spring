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
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    @Autowired
    private final GameService gameService;

    @PostMapping
    public ResponseEntity<GameResultResponse> saveGameResult(@Valid@RequestBody GameResultRequest request) {
        GameResultResponse response = gameService.saveGameResult(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{playerid}")
    public List<GameResultResponse> getGameResult(@PathVariable Long playerid) {
        return gameService.findByPlayerId(playerid);
    }

    @GetMapping("/recode/{user1id}/{user2id}")
    public relativeRecodeResponse getRelativeRecode(@PathVariable Long user1id, @PathVariable Long user2id){
        return gameService.findRelativeRecode(user1id, user2id);
    }
    
}
