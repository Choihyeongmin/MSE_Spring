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

    // login ID (중복 불가, 구글 로그인시 email이 아이디)
    @Column(unique = true, nullable = false)
    private String username;

    //salt
    @Column(nullable = false)
    private String salt;

    // login password
    @Column(nullable = true)
    private String password;

    // nickname (표시용 이름)
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
