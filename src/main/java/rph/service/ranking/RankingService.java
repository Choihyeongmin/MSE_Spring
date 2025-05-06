package rph.service.ranking;

import rph.dto.ranking.RankingResponse;
import java.util.List;

import rph.entity.User;

public interface RankingService {
    List<RankingResponse> getAllRankings();
    List<RankingResponse> getTopRankings(int count);
    void updateRanking(User player1, User player2, User winner, boolean draw);
}
