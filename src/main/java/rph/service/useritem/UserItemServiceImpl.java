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

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;

    @Override
    @Transactional  // ★ 중요! 트랜잭션 적용 (예외 발생 시 롤백)
    public UserItemResponse purchaseItem(UserItemRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.USER_NOT_FOUND));
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.ITEM_NOT_FOUND));

        Optional<UserItem> userItemOpt = userItemRepository.findByUserIdAndItemId(request.getUserId(), request.getItemId());
        UserItem userItem;

        if (!item.isStackable() && userItemOpt.isPresent()) {
            throw new UserItemException(UserItemErrorCode.ITEM_OWNED);
        }

        // ★ 여기서부터 안전하게 코인 차감
        if (user.getCoins() < item.getPrice()) {
            throw new UserItemException(UserItemErrorCode.NO_COIN);
        }

        user.setCoins(user.getCoins() - item.getPrice());
        userRepository.save(user);

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
        Optional<UserItem> userItem = userItemRepository.findByUserIdAndItemId(userId, itemId);
        return userItem.isPresent();  // 있으면 true, 없으면 false
    }


    @Override
    public List<UserItemResponse> getItemsByUserId(Long userId) {
        return userItemRepository.findByUserId(userId).stream()
                .map(UserItemResponse::from)
                .collect(Collectors.toList()); 
    }

}
