package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Model.Product;
import com.abcRestaurant.abcRestaurant.Service.ProductService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
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

    @GetMapping("/{id}")
    public ResponseEntity<Product> getSingleProduct(@PathVariable ObjectId id) {
        Optional<Product> product = productService.singleProduct(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product newProduct = productService.addProduct(product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") ObjectId id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") ObjectId id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Mark or unmark a product as favorite
    @PutMapping("/{id}/favorite")
    public ResponseEntity<Product> toggleFavorite(@PathVariable("id") ObjectId id) {
        Optional<Product> productOpt = productService.singleProduct(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();

            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        }
        return ResponseEntity.notFound().build();
    }


}
