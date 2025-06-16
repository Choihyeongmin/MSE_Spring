package rph.service.useritem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rph.dto.useritem.UserItemRequest;
import rph.dto.useritem.UserItemResponse;
import rph.entity.Item;
import rph.entity.User;
import rph.entity.UserItem;
import rph.exception.RestApiException;
import rph.exception.UserItemException;
import rph.exception.ErrorCode.CommonErrorCode;
import rph.exception.ErrorCode.UserItemErrorCode;
import rph.repository.ItemRepository;
import rph.repository.UserItemRepository;
import rph.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.Null;

@Service
@RequiredArgsConstructor
public class UserItemServiceImpl implements UserItemService {

    private final UserRepository userRepository; // Repository for user
    private final ItemRepository itemRepository; // Repository for item
    private final UserItemRepository userItemRepository; // Repository for user-item relationship

    @Override
    @Transactional
    public UserItemResponse purchaseItem(UserItemRequest request) {
        // Find user and item
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.USER_NOT_FOUND));
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.ITEM_NOT_FOUND));

        // Find if user already owns this item
        Optional<UserItem> userItemOpt = userItemRepository.findByUserIdAndItemId(request.getUserId(), request.getItemId());
        UserItem userItem;

        // If item is not stackable and already owned, throw exception
        if (!item.isStackable() && userItemOpt.isPresent()) {
            throw new UserItemException(UserItemErrorCode.ITEM_OWNED);
        }

        // Check coin balance
        if (user.getCoins() < item.getPrice()) {
            throw new UserItemException(UserItemErrorCode.NO_COIN);
        }

        // Deduct coins
        user.setCoins(user.getCoins() - item.getPrice());
        userRepository.save(user);

        // Save or update user item
        if (item.isStackable()) {
            if (userItemOpt.isPresent()) {
                userItem = userItemOpt.get();
                userItem.setCount(userItem.getCount() + 1);
            } else {
                userItem = new UserItem();
                userItem.setUser(user);
                userItem.setItem(item);
                userItem.setCount(1);
                userItem.setOwnedAt(LocalDateTime.now());
            }
        } else {
            userItem = new UserItem();
            userItem.setUser(user);
            userItem.setItem(item);
            userItem.setCount(1);
            userItem.setOwnedAt(LocalDateTime.now());
        }

        userItemRepository.save(userItem);

        return UserItemResponse.from(userItem);
    }

    @Override
    public boolean checkUserItem(Long userId, Long itemId) {
        // Check if user owns the item
        Optional<UserItem> userItem = userItemRepository.findByUserIdAndItemId(userId, itemId);
        return userItem.isPresent();  
    }

    @Override
    public List<UserItemResponse> getItemsByUserId(Long userId) {
        // Get all items owned by user
        return userItemRepository.findByUserId(userId).stream()
                .map(UserItemResponse::from)
                .collect(Collectors.toList()); 
    }

}