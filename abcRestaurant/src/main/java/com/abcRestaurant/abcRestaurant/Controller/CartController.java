package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Model.Cart;
import com.abcRestaurant.abcRestaurant.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // View cart products for a specific user
    @GetMapping("/view")
    public ResponseEntity<Cart> viewCart(@RequestParam String userId) {
        Cart cart = cartService.viewCart(userId);
        return ResponseEntity.ok(cart);
    }

    // Add or update a product in the cart
    @PostMapping("/addOrUpdate")
    public ResponseEntity<Cart> addOrUpdateItem(
            @RequestParam String userId,
            @RequestParam String productId,
            @RequestParam int quantity) {
        Cart updatedCart = cartService.addOrUpdateItem(userId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    // Remove a product from the cart
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeItem(
            @RequestParam String userId,
            @RequestParam String productId) {
        cartService.removeItem(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
