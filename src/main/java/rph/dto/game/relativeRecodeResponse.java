package rph.dto.game;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class relativeRecodeResponse {
    private int totalGames;
    private int user1Wins;
    private int user2Wins;
    private int draw;
}
