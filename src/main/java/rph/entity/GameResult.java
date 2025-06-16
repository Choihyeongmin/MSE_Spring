package rph.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity that stores the result of a game match.
 * Includes players, winner, score info, and time.
 */
@Entity
@Table(name = "game_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-generated primary key

    @ManyToOne
    @JoinColumn(name = "player1_id", nullable = false)
    private User player1; // First player

    @ManyToOne
    @JoinColumn(name = "player2_id", nullable = false)
    private User player2; // Second player

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private User winner; // Winner of the match (nullable if draw)

    private boolean draw; // True if the match was a draw

    private int tileOwnedPlayer1; // Number of tiles owned by player1
    private int tileOwnedPlayer2; // Number of tiles owned by player2

    private int scoreDiff; // Score difference between two players

    private LocalDateTime startTime; // Game start time,but Not used
    private LocalDateTime endTime;   // Game end time,but Not used
}
