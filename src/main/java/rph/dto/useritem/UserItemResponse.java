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
    private LocalDateTime ownedAt;

    public static UserItemResponse from(UserItem userItem) {
        return new UserItemResponse(
            userItem.getItem().getName(),
            userItem.getOwnedAt()
        );
    }
}
