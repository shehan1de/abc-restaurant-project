package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import com.abcRestaurant.abcRestaurant.Model.Product;
import com.abcRestaurant.abcRestaurant.Service.FileStorageService;
import com.abcRestaurant.abcRestaurant.Service.ProductService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productService.allProduct(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getSingleProduct(@PathVariable ObjectId id) {
        Optional<Product> product = productService.singleProduct(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> addProduct(
            @RequestParam("productName") String productName,
            @RequestParam("categoryName") String categoryName,
            @RequestParam("productPrice") float productPrice,
            @RequestParam("productDescription") String productDescription,
            @RequestParam("productImage") MultipartFile productImage) {

        try {
            String filename = generateUniqueFilename(productImage.getOriginalFilename());

            fileStorageService.saveFile(productImage.getBytes(), filename);

            Product product = new Product();
            product.setProductName(productName);
            product.setCategoryName(categoryName);
            product.setProductPrice(productPrice);
            product.setProductDescription(productDescription);
            product.setProductImage(filename);

            Product newProduct = productService.addProduct(product);
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);

        } catch (IOException e) {
            return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> updateProduct(
            @PathVariable("productId") String productId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Float productPrice,  // Corrected to Float
            @RequestParam(required = false) String productDescription,
            @RequestParam(value = "productImage", required = false) MultipartFile productImage) {

        Map<String, Object> response = new HashMap<>();
        try {
            Product updatedProduct = productService.updateProduct(productId, productName, categoryName, productPrice,
                    productDescription, productImage);

            response.put("status", "success");
            response.put("product", updatedProduct);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "File upload failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } catch (ResourceNotFoundException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/byCategory")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @RequestParam(value = "categoryName", required = false) String categoryName) {
        List<Product> products;
        if (categoryName != null && !categoryName.isEmpty()) {
            products = productService.getProductsByCategory(categoryName);
        } else {
            products = productService.allProduct();
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/byProductId/{productId}")
    public ResponseEntity<Product> getProductByProductId(@PathVariable("productId") String productId) {
        Optional<Product> product = productService.findProductByProductId(productId);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private String generateUniqueFilename(String originalFilename) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return "product-" + timestamp + extension;
    }


}
