package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.FavoriteProduct;
import com.abcRestaurant.abcRestaurant.Repository.FavoriteProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteProductService {

    @Autowired
    private FavoriteProductRepository favoriteProductRepository;

    // Add a product to the user's favorites
    public void addFavoriteProduct(String userId, String productId) {
        FavoriteProduct favoriteProduct = favoriteProductRepository.findByUserId(userId);

        if (favoriteProduct == null) {
            favoriteProduct = new FavoriteProduct();
            favoriteProduct.setUserId(userId);
            favoriteProduct.setProductIds(new ArrayList<>());
        }

        if (!favoriteProduct.getProductIds().contains(productId)) {
            favoriteProduct.getProductIds().add(productId);
            favoriteProductRepository.save(favoriteProduct);
        }
    }

    // Remove a product from the user's favorites
    public void removeFavoriteProduct(String userId, String productId) {
        FavoriteProduct favoriteProduct = favoriteProductRepository.findByUserId(userId);

        if (favoriteProduct != null) {
            favoriteProduct.getProductIds().remove(productId);
            // Remove the favoriteProduct document if the productIds list is empty
            if (favoriteProduct.getProductIds().isEmpty()) {
                favoriteProductRepository.delete(favoriteProduct);
            } else {
                favoriteProductRepository.save(favoriteProduct);
            }
        }
    }

    // Get all favorite product IDs for a user
    public List<String> getFavoriteProducts(String userId) {
        FavoriteProduct favoriteProduct = favoriteProductRepository.findByUserId(userId);
        return favoriteProduct != null ? favoriteProduct.getProductIds() : new ArrayList<>();
    }
}
