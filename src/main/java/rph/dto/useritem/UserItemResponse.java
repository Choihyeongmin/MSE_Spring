package rph.dto.useritem;

import lombok.*;
import rph.entity.UserItem;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserItemResponse {
    private String itemName;
    private Long itemId;
    private String name;
    private String description;
    private int price;
    private String type;
    private boolean isStackable;

    private LocalDateTime ownedAt;
    private int count;

    public static UserItemResponse from(UserItem userItem) {
        return new UserItemResponse(
            userItem.getItem().getName(),
            userItem.getItem().getId(),
            userItem.getItem().getName(),
            userItem.getItem().getDescription(),
            userItem.getItem().getPrice(),
            userItem.getItem().getType().name(),
            userItem.getItem().isStackable(),
            userItem.getOwnedAt(),
            userItem.getCount()
        );
    }
}
