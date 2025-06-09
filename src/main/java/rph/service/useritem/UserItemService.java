package rph.service.useritem;

import rph.dto.useritem.UserItemRequest;
import rph.dto.useritem.UserItemResponse;

import java.util.List;

public interface UserItemService {
    UserItemResponse purchaseItem(UserItemRequest request);
    List<UserItemResponse> getItemsByUserId(Long userId);
    boolean checkUserItem(Long userId, Long itemId);
}
