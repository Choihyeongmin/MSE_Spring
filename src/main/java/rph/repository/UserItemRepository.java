package rph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rph.entity.UserItem;

import java.util.List;

public interface UserItemRepository extends JpaRepository<UserItem, Long> {
    List<UserItem> findByUserId(Long userId);
}
