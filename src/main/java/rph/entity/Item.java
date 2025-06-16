package rph.entity;

import lombok.*;
import javax.persistence.*;

/**
 * Represents an item that users can purchase and own.
 * Includes properties like name, price, and type.
 */
@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-generated item ID

    private String name; // Item name

    private String description; // Item description (what it does)

    private int price; // Cost of the item (in coins)

    private boolean isStackable; // Whether the item can be owned multiple times

    @Enumerated(EnumType.STRING)
    private ItemType type; // Category/type of the item

    //only used Item
    public enum ItemType {
        SKIN, EFFECT, BOOST, ITEM
    }
}
