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

/**
 * Service implementation for item-related operations.
 * Handles item retrieval, creation, updating, and deletion.
 */
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository; // Repository to access item table

    /**
     * Get a list of all items.
     */
    @Override
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll().stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * Get a single item by its ID.
     */
    @Override
    public ItemResponse getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .map(ItemResponse::from)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.ITEM_NOT_FOUND));
    }

    /**
     * Get all items filtered by item type.
     * @param type Item type (e.g., "SKIN", "BOOST")
     */
    @Override
    public List<ItemResponse> getItemsByType(String type) {
        ItemType itemType = ItemType.valueOf(type.toUpperCase());
        return itemRepository.findByType(itemType).stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * Save a new item to the database.
     */
    @Override
    public ItemResponse saveItem(ItemRequest request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setDescription(request.getDescription());
        item.setType(ItemType.valueOf(request.getType().toUpperCase()));
        return ItemResponse.from(itemRepository.save(item));
    }

    /**
     * Update an existing item.
     * @param id ID of the item to update
     */
    @Override
    public ItemResponse updateItem(ItemRequest request, Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.ITEM_NOT_FOUND));

        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setDescription(request.getDescription());
        item.setType(ItemType.valueOf(request.getType().toUpperCase()));
        item.setStackable(request.isStackable());

        return ItemResponse.from(itemRepository.save(item));
    }

    /**
     * Delete an item by its ID.
     */
    @Override
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + id));
        itemRepository.delete(item);
    }
}
