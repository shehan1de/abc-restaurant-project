package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Branch;
import com.abcRestaurant.abcRestaurant.Model.Product;
import com.abcRestaurant.abcRestaurant.Repository.ProductRepository;
import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileStorageService fileStorageService;

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

    // ID generation
    private String generateProductId() {
        Optional<Product> latestProduct = productRepository.findTopByOrderByProductIdDesc();
        int maxId = 0;

        if (latestProduct.isPresent()) {
            String productId = latestProduct.get().getProductId();
            try {
                int numericPart = Integer.parseInt(productId.split("-")[1]);
                maxId = numericPart;
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.err.println("Error parsing productId: " + productId + ". Using default maxId.");
            }
        }

        int nextId = maxId + 1;
        return String.format("product-%03d", nextId);
    }

    public Product updateProduct(String productId, String productName, String categoryName, Float productPrice, String productDescription, MultipartFile productImage) throws IOException {
        Product existingProduct = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

        String productImageFilename = handleProductImageUpload(productImage);

        if (productName != null && !productName.isEmpty()) {
            existingProduct.setProductName(productName);
        }
        if (categoryName != null && !categoryName.isEmpty()) {
            existingProduct.setCategoryName(categoryName);
        }
        if (productPrice != null) {
            existingProduct.setProductPrice(productPrice);
        }
        if (productDescription != null && !productDescription.isEmpty()) {
            existingProduct.setProductDescription(productDescription);
        }
        if (productImageFilename != null) {
            existingProduct.setProductImage(productImageFilename);
        }

        return productRepository.save(existingProduct);
    }

    private String handleProductImageUpload(MultipartFile productImage) throws IOException {
        if (productImage != null && !productImage.isEmpty()) {
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String originalFilename = productImage.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }
            String productImageFilename = "product-" + timestamp + fileExtension;
            fileStorageService.saveFile(productImage.getBytes(), productImageFilename);
            return productImageFilename;
        }
        return null;
    }

    // Delete a product by id
    public void deleteProduct(String productId) {
        Product existingProduct = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));
        productRepository.delete(existingProduct);
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
