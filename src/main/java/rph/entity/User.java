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

    // DB 고유 ID (Primary Key)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 로그인용 아이디 (중복 불가, 구글 로그인시 email이 아이디)
    @Column(unique = true, nullable = false)
    private String username;

    //salt
    @Column(nullable = false)
    private String salt;

    // 로그인용 비밀번호
    @Column(nullable = true)
    private String password;

    // 닉네임 (표시용 이름)
    @Column(unique = true, nullable = false)
    private String nickname;

    //구글 로그인용
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
