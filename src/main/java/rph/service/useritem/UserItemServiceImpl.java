package rph.service.useritem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rph.dto.useritem.UserItemRequest;
import rph.dto.useritem.UserItemResponse;
import rph.entity.Item;
import rph.entity.User;
import rph.entity.UserItem;
import rph.repository.ItemRepository;
import rph.repository.UserItemRepository;
import rph.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserItemServiceImpl implements UserItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;

    @Override
    public UserItemResponse purchaseItem(UserItemRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (user.getCoins() < item.getPrice()) {
            throw new IllegalStateException("Not enough coins");
        }

        user.setCoins(user.getCoins() - item.getPrice());
        userRepository.save(user);

        UserItem userItem = new UserItem();
        userItem.setUser(user);
        userItem.setItem(item);
        userItem.setOwnedAt(LocalDateTime.now());
        userItemRepository.save(userItem);

        return UserItemResponse.from(userItem);
    }

    @Override
    public List<UserItemResponse> getItemsByUserId(Long userId) {
        return userItemRepository.findByUserId(userId).stream()
                .map(UserItemResponse::from)
                .collect(Collectors.toList());
    }
}
