package com.example.ecommerce.controller;

import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        CartItem cartItem = cartService.addToCart(user, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(cartItem);
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        List<CartItem> cartItems = cartService.getCartItems(user);
        return ResponseEntity.ok(cartItems);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long productId, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        cartService.removeFromCart(user, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        cartService.clearCart(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<CartItem> updateCartItemQuantity(@PathVariable Long productId, @RequestBody UpdateCartItemRequest request, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        CartItem updatedItem = cartService.updateCartItemQuantity(user, productId, request.getNewQuantity());
        return ResponseEntity.ok(updatedItem);
    }
}

class AddToCartRequest {
    private Long productId;
    private int quantity;


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

class UpdateCartItemRequest {
    private int newQuantity;

    public int getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }
}

