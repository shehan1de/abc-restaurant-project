package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Product;
import com.abcRestaurant.abcRestaurant.Repository.ProductRepository;
import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        product.setId(new ObjectId());  // Ensure ID is set before saving
        return productRepository.save(product);
    }

    // Update an existing product by id
    public Product updateProduct(ObjectId id, Product product) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }
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

    // Get products by list of IDs
    public List<Product> findProductsByIds(List<String> ids) {
        // Convert the list of string IDs to ObjectId
        List<ObjectId> objectIds = ids.stream()
                .map(ObjectId::new)
                .collect(Collectors.toList());

        return productRepository.findAllById(objectIds);
    }

    // Get a single product by product id

    public Optional<Product> findProductByProductId(String productId) {
        return productRepository.findByProductId(productId);
    }

}
