package rph.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing an item owned by a user.
 * Tracks which item, how many, and when it was acquired.
 */
@Entity
@Table(name = "user_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-generated primary key

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // The user who owns the item

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item; // The item that is owned

    private int count; // How many of this item the user owns

    private LocalDateTime ownedAt; // Timestamp when the item was first acquired
}
