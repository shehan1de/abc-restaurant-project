package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Cart;
import com.abcRestaurant.abcRestaurant.Model.Product;
import com.abcRestaurant.abcRestaurant.Repository.CartRepository;
import com.abcRestaurant.abcRestaurant.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // Method to add a product to the cart
    public void addToCart(String userId, String productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(new HashMap<>());
        }

        Map<String, Integer> products = cart.getProductId();
        products.put(productId, products.getOrDefault(productId, 0) + quantity);

        // Recalculate total amount after adding the product
        double totalAmount = calculateTotalAmount(products);
        cart.setTotalAmount(totalAmount);

        cartRepository.save(cart);
    }

    // Method to remove a product from the cart
    public void removeFromCart(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId);

        if (cart != null) {
            Map<String, Integer> products = cart.getProductId();
            if (products.containsKey(productId)) {
                products.remove(productId);

                // Recalculate total amount after removing the product
                double totalAmount = calculateTotalAmount(products);
                cart.setTotalAmount(totalAmount);

                cartRepository.save(cart);
                if (products.isEmpty()) {
                    cartRepository.deleteById(cart.getId().toString());
                }
            }
        }
    }

    // Method to get the cart details
    public Cart getCart(String userId) {
        return cartRepository.findByUserId(userId);
    }

    // Helper method to calculate the total amount
    private double calculateTotalAmount(Map<String, Integer> products) {
        double totalAmount = 0.0;

        // Loop through each product in the cart
        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();

            // Fetch the product price from the Product collection
            Optional<Product> productOpt = productRepository.findByProductId(productId);
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                double productPrice = product.getProductPrice();

                // Calculate total amount for the current product
                totalAmount += (productPrice * quantity);
            }
        }

        // Return the calculated total amount
        return totalAmount;
    }

}
