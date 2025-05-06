package rph.service.item;

import rph.dto.item.ItemResponse;
import java.util.List;

public interface ItemService {
    List<ItemResponse> getAllItems();
    ItemResponse getItemById(Long itemId);
    List<ItemResponse> getItemsByType(String type);
}
