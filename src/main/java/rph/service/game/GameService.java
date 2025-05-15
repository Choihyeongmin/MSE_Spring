package rph.service.game;

import java.util.List;

import rph.dto.game.GameResultRequest;
import rph.dto.game.GameResultResponse;
import rph.dto.game.relativeRecode;


public interface GameService {
    void saveGameResult(GameResultRequest request);
    List<GameResultResponse> findByPlayerId(Long playerId);
    relativeRecode findRelativeRecode(Long user1, Long user2);
}
