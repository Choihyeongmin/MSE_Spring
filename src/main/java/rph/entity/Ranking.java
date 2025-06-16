package rph.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a user's ranking record.
 * Tracks win/loss/draw stats and calculates points and rank.
 */
@Entity
@Table(name = "rankings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-generated primary key

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user; // The user this ranking belongs to

    private int totalGames;  // Total number of games played
    private int totalWins;   // Number of wins
    private int totalDraws;  // Number of draws
    private int totalLosses; // Number of losses
    private int totalPoints; // Total ranking points (e.g., win = 3, draw = 1)
    private int rank_;       // Current rank (e.g., 1st, 2nd, ...)

    private LocalDateTime lastUpdated; // Timestamp of last update

    /**
     * Increments win count, total games, and adds 3 points.
     */
    public void recordWin() {
        totalGames++;
        totalWins++;
        totalPoints += 3;
    }

    /**
     * Increments draw count, total games, and adds 1 point.
     */
    public void recordDraw() {
        totalGames++;
        totalDraws++;
        totalPoints += 1;
    }

    /**
     * Increments loss count and total games.
     */
    public void recordLoss() {
        totalGames++;
        totalLosses++;
    }
}
