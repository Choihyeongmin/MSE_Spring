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

/**
 * Service implementation for handling user-item actions.
 * Supports item purchasing, ownership check, and inventory retrieval.
 */
@Service
@RequiredArgsConstructor
public class UserItemServiceImpl implements UserItemService {

    private final UserRepository userRepository;             // User data access
    private final ItemRepository itemRepository;             // Item data access
    private final UserItemRepository userItemRepository;     // Inventory access

    /**
     * User purchases an item.
     * Handles coins, stackable logic, and saves user inventory.
     */
    @Override
    @Transactional
    public UserItemResponse purchaseItem(UserItemRequest request) {
        // Fetch user and item entities
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.USER_NOT_FOUND));
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.ITEM_NOT_FOUND));

        // Check if user already owns this item
        Optional<UserItem> userItemOpt = userItemRepository.findByUserIdAndItemId(request.getUserId(), request.getItemId());
        UserItem userItem;

        // Non-stackable items cannot be purchased again
        if (!item.isStackable() && userItemOpt.isPresent()) {
            throw new UserItemException(UserItemErrorCode.ITEM_OWNED);
        }

        // Check if user has enough coins
        if (user.getCoins() < item.getPrice()) {
            throw new UserItemException(UserItemErrorCode.NO_COIN);
        }

        // Deduct item cost from user's coins
        user.setCoins(user.getCoins() - item.getPrice());
        userRepository.save(user);

        // If stackable and already owned, increase count
        if (item.isStackable()) {
            if (userItemOpt.isPresent()) {
                userItem = userItemOpt.get();
                userItem.setCount(userItem.getCount() + 1);
            } else {
                // Create new inventory entry
                userItem = new UserItem();
                userItem.setUser(user);
                userItem.setItem(item);
                userItem.setCount(1);
                userItem.setOwnedAt(LocalDateTime.now());
            }
        } else {
            // Always create a new record for non-stackable
            userItem = new UserItem();
            userItem.setUser(user);
            userItem.setItem(item);
            userItem.setCount(1);
            userItem.setOwnedAt(LocalDateTime.now());
        }

        // Save user-item relationship
        userItemRepository.save(userItem);

        return UserItemResponse.from(userItem);
    }

    /**
     * Check if a user owns a specific item.
     */
    @Override
    public boolean checkUserItem(Long userId, Long itemId) {
        Optional<UserItem> userItem = userItemRepository.findByUserIdAndItemId(userId, itemId);
        return userItem.isPresent();  
    }

    /**
     * Get all items that a user currently owns.
     */
    @Override
    public List<UserItemResponse> getItemsByUserId(Long userId) {
        return userItemRepository.findByUserId(userId).stream()
                .map(UserItemResponse::from)
                .collect(Collectors.toList()); 
    }
}
