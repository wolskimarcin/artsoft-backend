package com.busher.artsoftbackend.api.controller;

import com.busher.artsoftbackend.api.dto.CartItemRequest;
import com.busher.artsoftbackend.model.Cart;
import com.busher.artsoftbackend.model.CartItem;
import com.busher.artsoftbackend.model.LocalUser;
import com.busher.artsoftbackend.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addProductToCart(@AuthenticationPrincipal LocalUser user,
                                                     @RequestBody CartItemRequest request) {
        CartItem cartItem = cartService.addProductToCart(user, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(cartItem);
    }

    @PutMapping("/item/{itemId}")
    public ResponseEntity<CartItem> updateCartItem(@AuthenticationPrincipal LocalUser user,
                                                   @PathVariable Long itemId,
                                                   @RequestBody CartItemRequest request) {
        CartItem updatedItem = cartService.updateCartItem(user, itemId, request.getQuantity());
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<?> removeCartItem(@AuthenticationPrincipal LocalUser user,
                                            @PathVariable Long itemId) {
        cartService.removeCartItem(user, itemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Cart> getCurrentCart(@AuthenticationPrincipal LocalUser user) {
        Cart cart = cartService.getCurrentCart(user);
        return ResponseEntity.ok(cart);
    }
}