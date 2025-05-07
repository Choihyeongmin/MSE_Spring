package rph.service.game;

import rph.dto.game.GameResultRequest;

public interface GameService {
    void saveGameResult(GameResultRequest request);
}
