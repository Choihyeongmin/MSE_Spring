package rph.service.ranking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rph.dto.ranking.RankingResponse;
import rph.repository.RankingRepository;
import rph.entity.Ranking;
import rph.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;

/**
 * Service for handling ranking-related logic.
 * Supports ranking updates after games and top-rank retrieval.
 */
@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final RankingRepository rankingRepository; // Ranking table access

    /**
     * Get all rankings sorted by total points (descending).
     */
    @Override
    public List<RankingResponse> getAllRankings() {
        return rankingRepository.findAllByOrderByTotalPointsDesc().stream()
                .map(RankingResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * Get top N ranked users.
     * @param count number of top users to return
     */
    @Override
    public List<RankingResponse> getTopRankings(int count) {
        return rankingRepository.findAllByOrderByTotalPointsDesc(PageRequest.of(0, count))
                .stream()
                .map(RankingResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * Update rankings of both players after a game.
     * Handles win/draw/loss logic and recalculates ranks.
     */
    @Override
    public void updateRanking(User player1, User player2, User winner, boolean draw) {
        updateSingle(player1, winner, draw);
        updateSingle(player2, winner, draw);
        recalculateAllRanks();
    }

    /**
     * Update or create a ranking record for a single user.
     */
    private void updateSingle(User user, User winner, boolean draw) {
        // Find existing ranking or create a new one
        Ranking ranking = rankingRepository.findByUser(user)
                .orElseGet(() -> Ranking.builder()
                        .user(user)
                        .totalGames(0)
                        .totalWins(0)
                        .totalDraws(0)
                        .totalLosses(0)
                        .totalPoints(0)
                        .rank_(0)
                        .lastUpdated(LocalDateTime.now())
                        .build());

        // Apply result
        if (draw) {
            ranking.recordDraw();
        } else if (winner != null && winner.getId().equals(user.getId())) {
            ranking.recordWin();
        } else {
            ranking.recordLoss();
        }

        ranking.setLastUpdated(LocalDateTime.now());
        rankingRepository.save(ranking);
    }

    /**
     * Recalculate and update all player ranks based on points.
     * Called after every match to keep leaderboard accurate.
     */
    private void recalculateAllRanks() {
        List<Ranking> all = rankingRepository.findAllByOrderByTotalPointsDesc();

        for (int i = 0; i < all.size(); i++) {
            all.get(i).setRank_(i + 1); // 1-based rank
        }

        rankingRepository.saveAll(all);
    }
}
