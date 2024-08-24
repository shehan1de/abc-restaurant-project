package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Cart;
import com.abcRestaurant.abcRestaurant.Model.Product;
import com.abcRestaurant.abcRestaurant.Repository.CartRepository;
import com.abcRestaurant.abcRestaurant.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;


    public void addToCart(String userId, String productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(new HashMap<>());
        }

        Map<String, Integer> products = cart.getProductId();

        products.put(productId, quantity);


        float totalAmount = (float) calculateTotalAmount(products);
        cart.setTotalAmount(totalAmount);

        cartRepository.save(cart);
    }

    public void removeFromCart(String userId, String productId, int quantityToRemove) {
        Cart cart = cartRepository.findByUserId(userId);

        if (cart != null) {
            Map<String, Integer> products = cart.getProductId();
            if (products.containsKey(productId)) {
                int currentQuantity = products.get(productId);
                int newQuantity = currentQuantity - quantityToRemove;

                if (newQuantity <= 0) {
                    products.remove(productId);
                } else {
                    products.put(productId, newQuantity);
                }

                float totalAmount = (float) calculateTotalAmount(products);
                cart.setTotalAmount(totalAmount);

                cartRepository.save(cart);

                if (products.isEmpty()) {
                    cartRepository.deleteById(cart.getId().toString());
                }
            }
        }
    }


    public Cart getCart(String userId) {
        return cartRepository.findByUserId(userId);
    }

    private double calculateTotalAmount(Map<String, Integer> products) {
        double totalAmount = 0.0;

        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();

            Optional<Product> productOpt = productRepository.findByProductId(productId);
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                double productPrice = product.getProductPrice();


                totalAmount += (productPrice * quantity);
            }
        }

        return totalAmount;
    }

    public Map<String, Object> getCartDetailsWithProductInfo(String userId) {
        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            return null;
        }

        Map<String, Integer> cartProducts = cart.getProductId();
        Map<String, Object> detailedCart = new HashMap<>();
        detailedCart.put("userId", userId);
        detailedCart.put("totalAmount", cart.getTotalAmount());

        List<Map<String, Object>> productDetailsList = cartProducts.entrySet().stream()
                .map(entry -> {
                    String productId = entry.getKey();
                    int quantity = entry.getValue();
                    Optional<Product> productOpt = productRepository.findByProductId(productId);

                    if (productOpt.isPresent()) {
                        Product product = productOpt.get();
                        Map<String, Object> productDetails = new HashMap<>();
                        productDetails.put("productId", productId);
                        productDetails.put("productName", product.getProductName());
                        productDetails.put("productImage", product.getProductImage());
                        productDetails.put("productDescription", product.getProductDescription());
                        productDetails.put("productPrice", product.getProductPrice());
                        productDetails.put("quantity", quantity);
                        return productDetails;
                    } else {
                        return null;
                    }
                })
                .filter(productDetails -> productDetails != null)
                .collect(Collectors.toList());

        detailedCart.put("products", productDetailsList);
        return detailedCart;
    }
}

