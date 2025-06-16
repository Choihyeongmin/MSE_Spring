package rph.service.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rph.dto.game.GameResultRequest;
import rph.dto.game.GameResultResponse;
import rph.dto.game.relativeRecodeResponse;
import rph.entity.GameResult;
import rph.exception.ErrorCode.CommonErrorCode;
import rph.exception.RestApiException;
import rph.entity.User;
import rph.entity.UserItem;
import rph.repository.GameResultRepository;
import rph.repository.UserItemRepository;
import rph.repository.UserRepository;
import rph.service.ranking.RankingService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameResultRepository gameResultRepository;
    private final UserRepository userRepository;
    private final UserItemRepository userItemRepository;
    private final RankingService rankingService;

    @Override
    public GameResultResponse saveGameResult(GameResultRequest request) {
        User player1 = userRepository.findById(request.getPlayer1Id())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.USER_NOT_FOUND));
        User player2 = userRepository.findById(request.getPlayer2Id())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.USER_NOT_FOUND));
        User winner = request.getWinnerId() == null ? null :
                userRepository.findById(request.getWinnerId())
                        .orElseThrow(() -> new RestApiException(CommonErrorCode.USER_NOT_FOUND));

        useItem(player1, 1L, request.getPlayer1UseItem1());
        useItem(player1, 2L, request.getPlayer1UseItem2());
        useItem(player1, 3L, request.getPlayer1UseItem3());

        useItem(player2, 1L, request.getPlayer2UseItem1());
        useItem(player2, 2L, request.getPlayer2UseItem2());
        useItem(player2, 3L, request.getPlayer2UseItem3());

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
                System.out.println("전");

        GameResultResponse response = GameResultResponse.from(gameResultRepository.save(gameResult));
        rankingService.updateRanking(player1, player2, winner, request.isDraw());

        rewardPlayers(player1, player2, winner, request.isDraw());
        return response;
    }

    @Override
    public List<GameResultResponse> findByPlayerId(Long playerId){
        return gameResultRepository.findByPlayer1_IdOrPlayer2_Id(playerId, playerId).stream()
                .map(GameResultResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public relativeRecodeResponse findRelativeRecode(Long user1, Long user2){
        List<GameResult> gameResults1 = gameResultRepository.findByPlayer1_IdAndPlayer2_Id(user1, user2);
        List<GameResult> gameResults2 = gameResultRepository.findByPlayer1_IdAndPlayer2_Id(user2, user1);

        int user1Win = 0, user2Win = 0, draw = 0, totalGames = 0;

        for (GameResult gr : gameResults1) {
            User winner = gr.getWinner();
            if (winner == null) {
                draw++;
            } else if (winner.getId().equals(user1)) {
                user1Win++;
            } else if (winner.getId().equals(user2)) {
                user2Win++;
            }
            totalGames++;
        }

        for (GameResult gr : gameResults2) {
            User winner = gr.getWinner();
            if (winner == null) {
                draw++;
            } else if (winner.getId().equals(user1)) {
                user1Win++;
            } else if (winner.getId().equals(user2)) {
                user2Win++;
            }
            totalGames++;
        }

        return new relativeRecodeResponse(totalGames, user1Win, user2Win, draw);
    }

    private void rewardPlayers(User player1, User player2, User winner, boolean draw) {
        if (draw) {
            player1.addExpAndCoins(50, 50);
            player2.addExpAndCoins(50, 50);
        } else {
            if (winner == null) {
                throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
            }

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

    private void useItem(User player, Long itemId, int countToUse) {
        if (countToUse <= 0) return;

        Optional<UserItem> userItemOpt = userItemRepository.findByUserIdAndItemId(player.getId(), itemId);
        UserItem userItem = userItemOpt.orElseThrow(() ->
                new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR));

        if (userItem.getCount() < countToUse) {
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
        }

        userItem.setCount(userItem.getCount() - countToUse);
        userItemRepository.save(userItem);
    }
}
