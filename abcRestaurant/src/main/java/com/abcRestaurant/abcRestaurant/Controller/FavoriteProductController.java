package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Model.FavoriteProduct;
import com.abcRestaurant.abcRestaurant.Service.FavoriteProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteProductController {

    @Autowired
    private FavoriteProductService favoriteProductService;



    @PostMapping("/add")
    public ResponseEntity<String> addFavoriteProduct(@RequestParam String userId, @RequestParam String productId) {
        try {
            favoriteProductService.addFavoriteProduct(userId, productId);
            return ResponseEntity.ok("Product added to favorites successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding product to favorites: " + e.getMessage());
        }
    }

    // Remove a product from the user's favorites
    @PostMapping("/remove")
    public ResponseEntity<String> removeFavoriteProduct(@RequestParam String userId, @RequestParam String productId) {
        try {
            favoriteProductService.removeFavoriteProduct(userId, productId);
            return ResponseEntity.ok("Product removed from favorites successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error removing product from favorites: " + e.getMessage());
        }
    }

    // Get all favorite product IDs for a user
    @GetMapping("/list")
    public ResponseEntity<List<String>> getFavoriteProducts(@RequestParam String userId) {
        try {
            List<String> favoriteProducts = favoriteProductService.getFavoriteProducts(userId);
            return ResponseEntity.ok(favoriteProducts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
