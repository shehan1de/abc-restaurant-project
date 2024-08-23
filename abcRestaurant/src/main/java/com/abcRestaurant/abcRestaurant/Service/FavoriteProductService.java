package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.FavoriteProduct;
import com.abcRestaurant.abcRestaurant.Model.Product;
import com.abcRestaurant.abcRestaurant.Repository.FavoriteProductRepository;
import com.abcRestaurant.abcRestaurant.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteProductService {

    @Autowired
    private FavoriteProductRepository favoriteProductRepository;

    @Autowired
    private ProductRepository productRepository;

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

            // Save changes to the repository
            favoriteProductRepository.save(favoriteProduct);
            if (productIds.isEmpty()) {
                favoriteProductRepository.deleteById(favoriteProduct.getId().toString());
            }
        }
    }
    public List<String> getFavoriteProducts(String userId) {
        FavoriteProduct favoriteProduct = favoriteProductRepository.findByUserId(userId);
        return favoriteProduct != null ? favoriteProduct.getProductId() : new ArrayList<>();
    }

    public List<Product> getFavoriteProductDetails(String userId) {
        FavoriteProduct favoriteProduct = favoriteProductRepository.findByUserId(userId);
        List<Product> favoriteProducts = new ArrayList<>();

        if (favoriteProduct != null && favoriteProduct.getProductId() != null) {
            for (String productId : favoriteProduct.getProductId()) {
                Optional<Product> product = productRepository.findByProductId(productId); // Use Optional to handle null
                product.ifPresent(favoriteProducts::add); // If product is present, add it to the list
            }
        }

        return favoriteProducts;
    }


}
