package rph.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private int price;

    private boolean isStackable;  // ★ 중복 구매 가능 여부 (true = 가능)

    @Enumerated(EnumType.STRING)
    private ItemType type;

    public enum ItemType {
        SKIN, EFFECT, BOOST, ITEM
    }
}
