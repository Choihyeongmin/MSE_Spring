package rph.service.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import rph.dto.item.ItemRequest;
import rph.dto.item.ItemResponse;
import rph.entity.Item;
import rph.entity.Item.ItemType;
import rph.exception.ErrorCode.CommonErrorCode;
import rph.exception.RestApiException;
import rph.repository.ItemRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository; // Repository for item data

    @Override
    public List<ItemResponse> getAllItems() {
        // Return all items
        return itemRepository.findAll().stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponse getItemById(Long itemId) {
        // Find item by ID or throw exception
        return itemRepository.findById(itemId)
                .map(ItemResponse::from)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.ITEM_NOT_FOUND));
    }

    @Override
    public List<ItemResponse> getItemsByType(String type) {
        // Find items by type
        ItemType itemType = ItemType.valueOf(type.toUpperCase());
        return itemRepository.findByType(itemType).stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponse saveItem(ItemRequest request){
        // Save new item
        Item item = new Item();
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setDescription(request.getDescription());
        item.setType(ItemType.valueOf(request.getType().toUpperCase()));
        return ItemResponse.from(itemRepository.save(item));
    }

    @Override
    public ItemResponse updateItem(ItemRequest request, Long id) {
        // Update existing item
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.ITEM_NOT_FOUND));

        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setDescription(request.getDescription());
        item.setType(ItemType.valueOf(request.getType().toUpperCase()));
        item.setStackable(request.isStackable());

        return ItemResponse.from(itemRepository.save(item));
    }

    @Override
    public void deleteItem(Long id) {
        // Delete item by ID
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + id));

        itemRepository.delete(item);
    }
}
