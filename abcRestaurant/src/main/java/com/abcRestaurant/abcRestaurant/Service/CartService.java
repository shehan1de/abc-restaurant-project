package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Cart;
import com.abcRestaurant.abcRestaurant.Repository.CartRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    // View cart products
    public Cart viewCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            return new Cart(); // Return an empty cart if not found
        }
        return cart;
    }

    // Add or update product quantity in cart
    public Cart addOrUpdateItem(String userId, String productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
        }
        Map<String, Integer> products = cart.getProducts();
        if (products == null) {
            products = new HashMap<>();
        }
        if (quantity <= 0) {
            products.remove(productId);
        } else {
            products.put(productId, quantity);
        }
        cart.setProducts(products);
        return cartRepository.save(cart);
    }

    // Remove item from cart
    public void removeItem(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            Map<String, Integer> products = cart.getProducts();
            if (products != null) {
                products.remove(productId);
                cart.setProducts(products);
                cartRepository.save(cart);
            }
        }
    }
}
