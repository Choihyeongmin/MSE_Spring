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
    private Long winnerId;         // 무승부일 경우 null 허용
    private boolean draw;          // true이면 무승부
    private int tileOwnedPlayer1;
    private int tileOwnedPlayer2;
    // player1 사용 아이템
    private int player1UseItem1;
    private int player1UseItem2;
    private int player1UseItem3;

    // player2 사용 아이템
    private int player2UseItem1;
    private int player2UseItem2;
    private int player2UseItem3;

    @Max(100)
    private int scoreDiff;
}
