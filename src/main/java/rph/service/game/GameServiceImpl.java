package rph.service.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rph.dto.game.GameResultRequest;
import rph.entity.GameResult;
import rph.entity.User;
import rph.repository.GameResultRepository;
import rph.repository.UserRepository;
import rph.service.ranking.RankingService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameResultRepository gameResultRepository;
    private final UserRepository userRepository;
    private final RankingService rankingService;

    @Override
    public void saveGameResult(GameResultRequest request) {
        User player1 = userRepository.findById(request.getPlayer1Id())
                .orElseThrow(() -> new IllegalArgumentException("player1 not found"));
        User player2 = userRepository.findById(request.getPlayer2Id())
                .orElseThrow(() -> new IllegalArgumentException("player2 not found"));
        User winner = request.getWinnerId() == null ? null :
                userRepository.findById(request.getWinnerId())
                .orElseThrow(() -> new IllegalArgumentException("winner not found"));

        GameResult gameResult = new GameResult();
        gameResult.setPlayer1(player1);
        gameResult.setPlayer2(player2);
        gameResult.setWinner(winner);
        gameResult.setDraw(request.isDraw());
        gameResult.setTileOwnedPlayer1(request.getTileOwnedPlayer1());
        gameResult.setTileOwnedPlayer2(request.getTileOwnedPlayer2());
        gameResult.setScoreDiff(request.getScoreDiff());
        gameResult.setStartTime(LocalDateTime.now());
        gameResult.setEndTime(LocalDateTime.now());

        gameResultRepository.save(gameResult);
        rankingService.updateRanking(player1, player2, winner, request.isDraw());
        rewardPlayers(player1, player2, winner, request.isDraw());
    }

    private void rewardPlayers(User player1, User player2, User winner, boolean draw) {
        if (draw) {
            player1.addExpAndCoins(50, 50);
            player2.addExpAndCoins(50, 50);
        } else {
            if (winner.getId().equals(player1.getId())) {
                player1.addExpAndCoins(100, 100);
                player2.addExpAndCoins(20, 20);
            } else {
                player1.addExpAndCoins(20, 20);
                player2.addExpAndCoins(100, 100);
            }
        }
    
        userRepository.save(player1);
        userRepository.save(player2);
    }
}
