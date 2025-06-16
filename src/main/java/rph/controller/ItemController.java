package rph.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rph.dto.item.ItemResponse;
import rph.service.item.ItemService;
import rph.dto.item.ItemRequest;
import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService; // Service for item operations

    @GetMapping
    public List<ItemResponse> getAllItems() {
        // Get all items
        return itemService.getAllItems();
    }

    @GetMapping("/{itemId}")
    public ItemResponse getItemDetail(@PathVariable Long itemId) {
        // Get item detail by ID
        return itemService.getItemById(itemId);
    }

    @GetMapping("/type/{type}")
    public List<ItemResponse> getItemsByType(@PathVariable String type) {
        // Get items by type
        return itemService.getItemsByType(type);
    }
}