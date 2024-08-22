package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Model.Product;
import com.abcRestaurant.abcRestaurant.Service.FavoriteProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteProductController {

    @Autowired
    private FavoriteProductService favoriteProductService;



    @PostMapping("/add")
    public void addFavoriteProduct(@RequestParam String userId, @RequestParam String productId) {
        favoriteProductService.addFavoriteProduct(userId, productId);
    }

    @PostMapping("/remove")
    public void removeFavoriteProduct(@RequestParam String userId, @RequestParam String productId) {
        favoriteProductService.removeFavoriteProduct(userId, productId);
    }

    @GetMapping("/list")
    public List<String> getFavoriteProducts(@RequestParam String userId) {
        return favoriteProductService.getFavoriteProducts(userId);
    }

}
