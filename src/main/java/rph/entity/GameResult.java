package rph.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "game_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    @ManyToOne
    @JoinColumn(name = "winner_id", nullable = false)
    private User winner;

    @ManyToOne
    @JoinColumn(name = "loser_id", nullable = false)
    private User loser;

    private double territory1;  // 점령률 1 - 이게 뭐더라
    private double territory2;  // 점령률 2
}
