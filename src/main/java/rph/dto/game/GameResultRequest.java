package rph.dto.game;

import javax.validation.constraints.Max;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameResultRequest {
    private Long player1Id;
    private Long player2Id;
    private Long winnerId;         // if draw, null
    private boolean draw;          // true is draw
    private int tileOwnedPlayer1;
    private int tileOwnedPlayer2;
    // player1 used item
    private int player1UseItem1;
    private int player1UseItem2;
    private int player1UseItem3;

    // player2 used item
    private int player2UseItem1;
    private int player2UseItem2;
    private int player2UseItem3;

    @Max(100)
    private int scoreDiff;
}
