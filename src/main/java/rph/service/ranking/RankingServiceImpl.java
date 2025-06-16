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

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final RankingRepository rankingRepository; // Repository for ranking data

    @Override
    public List<RankingResponse> getAllRankings() {
        // Return all rankings sorted by points
        return rankingRepository.findAllByOrderByTotalPointsDesc().stream()
                .map(RankingResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<RankingResponse> getTopRankings(int count) {
        // Return top 'count' rankings
        return rankingRepository.findAllByOrderByTotalPointsDesc(PageRequest.of(0, count))
                .stream()
                .map(RankingResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public void updateRanking(User player1, User player2, User winner, boolean draw) {
        // Update rankings for both players
        updateSingle(player1, winner, draw);
        updateSingle(player2, winner, draw);
        recalculateAllRanks();
    }

    private void updateSingle(User user, User winner, boolean draw) {
        // Get or create ranking for user
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

        // Update record depending on result
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

    private void recalculateAllRanks() {
        // Reassign ranks based on total points
        List<Ranking> all = rankingRepository.findAllByOrderByTotalPointsDesc();

        for (int i = 0; i < all.size(); i++) {
            all.get(i).setRank_(i + 1);
        }
        rankingRepository.saveAll(all);
    }
}
