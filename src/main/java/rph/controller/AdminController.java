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
    private ItemService itemService;
     @Autowired
    private UserService userService;


    @PostMapping ("/item")
    public ResponseEntity<ItemResponse> saveItem(@Valid@RequestBody ItemRequest item){
        ItemResponse response  = itemService.saveItem(item);  // ItemServie
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<String> deleteUserByAdmin(@PathVariable String username) {
    userService.deleteUserByAdmin(username);   // UserService
    return ResponseEntity.ok("User '" + username + "' has been deleted by admin.");
    }

}
