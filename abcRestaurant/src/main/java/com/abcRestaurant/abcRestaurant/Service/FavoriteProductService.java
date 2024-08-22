package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.FavoriteProduct;
import com.abcRestaurant.abcRestaurant.Model.Product;
import com.abcRestaurant.abcRestaurant.Repository.FavoriteProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteProductService {

    @Autowired
    private FavoriteProductRepository favoriteProductRepository;
    private ProductService productService;

    public void addFavoriteProduct(String userId, String productId) {
        FavoriteProduct favoriteProduct = favoriteProductRepository.findByUserId(userId);

        if (favoriteProduct == null) {
            favoriteProduct = new FavoriteProduct();
            favoriteProduct.setUserId(userId);
            favoriteProduct.setProductId(new ArrayList<>());
        }

        List<String> productIds = favoriteProduct.getProductId();
        if (!productIds.contains(productId)) {
            productIds.add(productId);
        }

        favoriteProductRepository.save(favoriteProduct);
    }

    public void removeFavoriteProduct(String userId, String productId) {
        FavoriteProduct favoriteProduct = favoriteProductRepository.findByUserId(userId);

        if (favoriteProduct != null) {
            List<String> productIds = favoriteProduct.getProductId();
            productIds.remove(productId);

            favoriteProductRepository.save(favoriteProduct);
        }
    }

    public List<String> getFavoriteProducts(String userId) {
        FavoriteProduct favoriteProduct = favoriteProductRepository.findByUserId(userId);
        return favoriteProduct != null ? favoriteProduct.getProductId() : new ArrayList<>();
    }

    public List<Product> getFavoriteProductDetails(String userId) {
        FavoriteProduct favoriteProduct = favoriteProductRepository.findByUserId(userId);
        if (favoriteProduct != null && favoriteProduct.getProductId() != null && !favoriteProduct.getProductId().isEmpty()) {
            return productService.findProductsByIds(favoriteProduct.getProductId());
        } else {
            return new ArrayList<>();
        }
    }


}
