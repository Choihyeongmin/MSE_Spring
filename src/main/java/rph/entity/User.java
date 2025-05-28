package rph.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    // DB innate ID (Primary Key)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Login ID (Username must be unique, if Google login, Username == Email)
    @Column(unique = true, nullable = false)
    private String username;

    //Salt
    @Column(nullable = false)
    private String salt;

    // Login password
    @Column(nullable = true)
    private String password;

    // Nickname (Display Name)
    @Column(unique = true, nullable = false)
    private String nickname;

    // Google login ID
    @Column(unique = true)
    private String googleId;

    private int exp = 0;
    private int level = 1;
    private int coins = 0;

    private String role; 

    public void addExpAndCoins(int expGain, int coinGain) {
        this.exp += expGain;
        this.coins += coinGain;
    
        while (this.exp >= 100) {
            this.exp -= 100;
            this.level += 1;
        }
    }
}
