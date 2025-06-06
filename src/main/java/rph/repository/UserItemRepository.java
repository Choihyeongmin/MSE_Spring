package rph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rph.entity.UserItem;

import java.util.List;
import java.util.Optional;

public interface UserItemRepository extends JpaRepository<UserItem, Long> {
    List<UserItem> findByUserId(Long userId);
    Optional<UserItem> findByUserIdAndItemId(Long userId, Long itemId);
}
