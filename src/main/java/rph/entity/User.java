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

    // 로그인용 아이디 (중복 불가)
    @Column(unique = true, nullable = false)
    private String username;

    // 로그인용 비밀번호
    @Column(nullable = false)
    private String password;

    // 닉네임 (표시용 이름)
    @Column(unique = true, nullable = false)
    private String nickname;

    private int exp = 0;
    private int level = 1;
    private int coins = 0;
}
