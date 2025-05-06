package rph.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "rank")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int wins = 0;
    private int losses = 0;
    private int draws = 0;
    private int score = 0;

    public double getWinRate() {
        int total = wins + losses + draws;
        return total == 0 ? 0.0 : (double) wins / total;
    }
}
