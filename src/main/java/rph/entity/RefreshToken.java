package rph.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    private String username; // 또는 userId

    @Column(nullable = false)
    private String refreshToken;
}
