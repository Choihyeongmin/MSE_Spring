package rph.service.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import rph.dto.item.ItemRequest;
import rph.dto.item.ItemResponse;
import rph.entity.Item;
import rph.entity.Item.ItemType;
import rph.exception.CommonErrorCode;
import rph.exception.RestApiException;
import rph.repository.ItemRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll().stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponse getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .map(ItemResponse::from)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.ITEM_NOT_FOUND));
    }

    @Override
    public List<ItemResponse> getItemsByType(String type) {
        ItemType itemType = ItemType.valueOf(type.toUpperCase());
        return itemRepository.findByType(itemType).stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public void saveItem(ItemRequest request){
        Item item = new Item();
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setDescription(request.getDescription());
        item.setType(ItemType.valueOf(request.getType().toUpperCase()));
        itemRepository.save(item);
    }
}
