package rph.dto.item;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    private String name;
    private String description;
    private int price;
    private String type;
    private boolean isStackable;
}
