package rph.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private int count;  // ★ 추가: 보유 개수

    private LocalDateTime ownedAt;

}
