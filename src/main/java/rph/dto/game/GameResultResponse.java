package rph.dto.game;

import javax.validation.constraints.Max;

import lombok.*;
import rph.entity.GameResult;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameResultResponse {
    private Long id;
    private Long player1Id;
    private Long player2Id;
    private Long winnerId;         // if draw, null
    private boolean draw;          // true is draw
    private int tileOwnedPlayer1;
    private int tileOwnedPlayer2;
    private int scoreDiff;

    public static GameResultResponse from(GameResult gameResult) {
        return new GameResultResponse(
            gameResult.getId(),
            gameResult.getPlayer1().getId(),
            gameResult.getPlayer2().getId(),
            gameResult.getWinner().getId(),
            gameResult.isDraw(),
            gameResult.getTileOwnedPlayer1(),
            gameResult.getTileOwnedPlayer2(),
            gameResult.getScoreDiff()
        );
    }
}

