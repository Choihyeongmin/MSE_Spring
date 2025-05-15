package rph.service.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rph.dto.game.GameResultRequest;
import rph.dto.game.GameResultResponse;
import rph.dto.game.relativeRecode;
import rph.entity.GameResult;
import rph.exception.CommonErrorCode;
import rph.exception.RestApiException;
import rph.entity.User;
import rph.repository.GameResultRepository;
import rph.repository.UserRepository;
import rph.service.ranking.RankingService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    
    @Override
    public List<GameResultResponse> findByPlayerId(Long playerId){
            System.out.println("리포지토리\n");

        // userRepository.findById(playerId)
        // .orElseThrow(() -> new RestApiException(CommonErrorCode.USER_NOT_FOUND));
        return gameResultRepository.findByPlayer1_IdOrPlayer2_Id(playerId, playerId).stream()
            .map(GameResultResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public relativeRecode findRelativeRecode(Long user1, Long user2){
        List<GameResult> gameResults1 = gameResultRepository.findByPlayer1_IdAndPlayer2_Id(user1, user2);
        List<GameResult> gameResults2 = gameResultRepository.findByPlayer1_IdAndPlayer2_Id(user2, user1);

        int user1Win=0;
        int user2Win=0;
        int draw=0;
        int totalGames=0;
        for(int i=0;i<gameResults1.size();i++){
            if(gameResults1.get(i).getWinner().getId().equals(user1)){
                user1Win++;
            }else if(gameResults1.get(i).getWinner().getId().equals(user2)){
                user2Win++;
            }else{
                draw++;
            }
            totalGames++;
        }
        for(int i=0;i<gameResults2.size();i++){
            if(gameResults2.get(i).getWinner().getId().equals(user1)){
                user1Win++;
            }else if(gameResults2.get(i).getWinner().getId().equals(user2)){
                user2Win++;
            }else{
                draw++;
            }
            totalGames++;
        }
        return (new relativeRecode(totalGames, user1Win, user2Win, draw));
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
