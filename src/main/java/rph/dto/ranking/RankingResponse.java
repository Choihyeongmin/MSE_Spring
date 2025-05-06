package rph.dto.ranking;

import lombok.*;
import rph.entity.Ranking;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RankingResponse {
    private String nickname;
    private int totalPoints;
    private int rank_;

    public static RankingResponse from(Ranking ranking) {
        return new RankingResponse(
            ranking.getUser().getNickname(),
            ranking.getTotalPoints(),
            ranking.getRank_()
        );
    }
}
