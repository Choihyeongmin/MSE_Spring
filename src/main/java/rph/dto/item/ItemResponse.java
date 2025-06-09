package rph.dto.item;

import lombok.*;
import rph.entity.Item;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private int price;
    private String type;
    private boolean isStackable;

    public static ItemResponse from(Item item) {
        return new ItemResponse(
            item.getId(),
            item.getName(),
            item.getDescription(),
            item.getPrice(),
            item.getType().name(),
            item.isStackable()
        );
    }
}
