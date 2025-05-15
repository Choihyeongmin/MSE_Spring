package rph.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rph.dto.game.GameResultRequest;
import rph.dto.game.GameResultResponse;
import rph.dto.game.relativeRecode;
import rph.service.game.GameService;


@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    @Autowired
    private final GameService gameService;

    @PostMapping
    public ResponseEntity<?> saveGameResult(@Valid@RequestBody GameResultRequest request) {
        gameService.saveGameResult(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{playerid}")
    public List<GameResultResponse> getGameResult(@PathVariable Long playerid) {
        return gameService.findByPlayerId(playerid);
    }

    @GetMapping("/recode/{user1}/{user2}")
    public relativeRecode getRelativeRecode(@PathVariable Long user1, @PathVariable Long user2){
        return gameService.findRelativeRecode(user1, user2);
    }
    
}
