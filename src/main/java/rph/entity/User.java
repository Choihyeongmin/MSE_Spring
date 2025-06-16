package rph.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Entity representing a user account.
 * Includes login credentials, nickname, level, and role.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key (auto-incremented)

    @Column(unique = true, nullable = false)
    private String username; // Login ID (can be email if using Google login)

    @Column(nullable = true)
    private String salt; // Salt for password hashing (null for Google login users)

    @Column(nullable = true)
    private String password; // Hashed password (nullable if Google login)

    @Column(unique = true, nullable = false)
    private String nickname; // Display name shown in the game

    @Column(unique = true)
    private String googleId; // ID for Google OAuth login (optional)

    private int exp = 0;   // Experience points
    private int level = 1; // User level
    private int coins = 0; // Coin balance

    private String role;   // Role (e.g., ROLE_USER, ROLE_ADMIN)

    /**
     * Adds experience and coins to the user.
     * Levels up for every 100 EXP.
     *
     * @param expGain   amount of EXP to add
     * @param coinGain  amount of coins to add
     */
    public void addExpAndCoins(int expGain, int coinGain) {
        this.exp += expGain;
        this.coins += coinGain;

        while (this.exp >= 100) {
            this.exp -= 100;
            this.level += 1;
        }
    }
}
