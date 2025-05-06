package rph.entity;

import lombok.*;
import javax.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "game_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
