package rph.dto.useritem;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserItemRequest {
    private Long userId;
    private Long itemId;
}
