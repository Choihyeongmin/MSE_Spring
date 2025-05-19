package rph.service.game;

import java.util.List;

import rph.dto.game.GameResultRequest;
import rph.dto.game.GameResultResponse;
import rph.dto.game.relativeRecodeResponse;


public interface GameService {
    GameResultResponse saveGameResult(GameResultRequest request);
    List<GameResultResponse> findByPlayerId(Long playerId);
    relativeRecodeResponse findRelativeRecode(Long user1, Long user2);
}
