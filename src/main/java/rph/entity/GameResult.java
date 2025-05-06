package rph.entity;

import lombok.*;
import javax.persistence.*;
<<<<<<< HEAD

@Entity
@Table(name = "game_result")
=======
import java.time.LocalDateTime;

@Entity
@Table(name = "game_results")
>>>>>>> Dev
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
<<<<<<< HEAD
    private Long gameId;

    @ManyToOne
    @JoinColumn(name = "winner_id", nullable = false)
    private User winner;

    @ManyToOne
    @JoinColumn(name = "loser_id", nullable = false)
    private User loser;

    private double territory1;  // 점령률 1 - 이게 뭐더라
    private double territory2;  // 점령률 2
=======
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player1_id", nullable = false)
    private User player1;

    @ManyToOne
    @JoinColumn(name = "player2_id", nullable = false)
    private User player2;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private User winner;

    private boolean draw;

    private int tileOwnedPlayer1;
    private int tileOwnedPlayer2;
    private int scoreDiff;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
>>>>>>> Dev
}
