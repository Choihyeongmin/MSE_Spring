package rph.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rph.dto.item.ItemResponse;
import rph.service.item.ItemService;
import rph.dto.item.ItemRequest;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemResponse> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{itemId}")
    public ItemResponse getItemDetail(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping("/type/{type}")
    public List<ItemResponse> getItemsByType(@PathVariable String type) {
        return itemService.getItemsByType(type);
    }

    @PostMapping("")
    public void saveItem(@RequestBody ItemRequest item){
        itemService.saveItem(item);
    }

}
