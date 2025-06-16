package rph.controller;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import rph.dto.item.ItemRequest;
import rph.dto.item.ItemResponse;
import rph.service.UserService;
import rph.service.item.ItemService;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private ItemService itemService; // Service for item operations

    @Autowired
    private UserService userService; // Service for user operations

    @PostMapping("/item")
    public ResponseEntity<ItemResponse> saveItem(@Valid @RequestBody ItemRequest item) {
        // Create new item
        ItemResponse response = itemService.saveItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<String> deleteUserByAdmin(@PathVariable String username) {
        // Admin deletes a user by username
        userService.deleteUserByAdmin(username);   
        return ResponseEntity.ok("User '" + username + "' has been deleted by admin.");
    }

    @PutMapping("/item/update/{id}")
    public ResponseEntity<ItemResponse> putMethodName(@PathVariable Long id, @Valid @RequestBody ItemRequest item) {
        // Update existing item
        ItemResponse response = itemService.updateItem(item, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/item/delete/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        // Delete item by ID
        itemService.deleteItem(id);  
        return ResponseEntity.noContent().build();
    }
}
