package rph.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rph.dto.useritem.UserItemRequest;
import rph.dto.useritem.UserItemResponse;
import rph.service.useritem.UserItemService;

import java.util.List;

@RestController
@RequestMapping("/user-items")
@RequiredArgsConstructor
public class UserItemController {
    private final UserItemService userItemService;
    @PostMapping
    public ResponseEntity<UserItemResponse> purchaseItem(@RequestBody UserItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userItemService.purchaseItem(request));
    }

    @GetMapping("/{userId}")
    public List<UserItemResponse> getUserItems(@PathVariable Long userId) {
        return userItemService.getItemsByUserId(userId);
    }
}
