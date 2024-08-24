package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Model.Cart;
import com.abcRestaurant.abcRestaurant.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(
            @RequestParam String userId,
            @RequestParam String productId,
            @RequestParam int quantity) {
        cartService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromCart(
            @RequestParam String userId,
            @RequestParam String productId,
            @RequestParam int quantity) {
        cartService.removeFromCart(userId, productId, quantity);
        return ResponseEntity.ok().build();
    }



    @GetMapping("/details")
    public ResponseEntity<Cart> getCartDetails(@RequestParam String userId) {
        Cart cart = cartService.getCart(userId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping("/detailsInfo")
    public ResponseEntity<Map<String, Object>> getCartDetailsWithProductInfo(@RequestParam String userId) {
        Map<String, Object> cartDetails = cartService.getCartDetailsWithProductInfo(userId);

        if (cartDetails != null) {
            return new ResponseEntity<>(cartDetails, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}

