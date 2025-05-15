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
    private Long winnerId;         // 무승부일 경우 null 허용
    private boolean draw;          // true이면 무승부
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

