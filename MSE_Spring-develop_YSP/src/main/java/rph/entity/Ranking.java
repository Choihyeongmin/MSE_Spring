package rph.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private int totalGames;
    private int totalWins;
    private int totalDraws;
    private int totalLosses;
    private int totalPoints;
    private int rank_;

    private LocalDateTime lastUpdated;

    public void recordWin() {
        totalGames++;
        totalWins++;
        totalPoints += 3;
    }

    public void recordDraw() {
        totalGames++;
        totalDraws++;
        totalPoints += 1;
    }

    public void recordLoss() {
        totalGames++;
        totalLosses++;
    }
}
