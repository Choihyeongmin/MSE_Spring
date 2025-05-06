package rph.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import rph.entity.Item;
import rph.entity.Item.ItemType;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByType(ItemType type);
}
