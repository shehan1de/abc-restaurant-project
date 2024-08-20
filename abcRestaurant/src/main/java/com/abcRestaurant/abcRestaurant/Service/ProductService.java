package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Product;
import com.abcRestaurant.abcRestaurant.Repository.ProductRepository;
import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    // Get all products
    public List<Product> allProduct() {
        return productRepository.findAll();
    }

    // Get a single product by id
    public Optional<Product> singleProduct(ObjectId id) {
        return productRepository.findById(id);
    }

    // Add a new product
    public Product addProduct(Product product) {
        product.setProductId(generateProductId());
        return productRepository.save(product);
    }

    // Generate a new product ID
    private String generateProductId() {
        long count = productRepository.count();
        return String.format("product-%03d", count + 1);
    }

    // Update an existing product by id
    public Product updateProduct(ObjectId id, Product product) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }
        // Ensure the ID in the request body matches the ID in the URL
        product.setId(id);
        return productRepository.save(product);
    }

    // Delete a product by id
    public void deleteProduct(ObjectId id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }
        productRepository.deleteById(id);
    }

    // Get products by category name
    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository.findByCategoryName(categoryName);
    }


}
