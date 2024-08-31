package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import com.abcRestaurant.abcRestaurant.Model.Category;
import com.abcRestaurant.abcRestaurant.Model.Product;
import com.abcRestaurant.abcRestaurant.Repository.CategoryRepository;
import com.abcRestaurant.abcRestaurant.Service.CategoryService;
import com.abcRestaurant.abcRestaurant.Service.FileStorageService;
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
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileStorageService fileStorageService;


    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return new ResponseEntity<>(categoryService.allCategory(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Optional<Category>> getSingleCategory(@PathVariable ObjectId id) {
        return new ResponseEntity<>(categoryService.singleCategory(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addCategory(

            @RequestParam("categoryName") String categoryName,
            @RequestParam("categoryDescription") String categoryDescription,
            @RequestParam("categoryImage") MultipartFile categoryImage) {

        try {
            // Generate a unique filename for the image
            String filename = generateUniqueFilename(categoryImage.getOriginalFilename());

            // Save the file
            fileStorageService.saveFile(categoryImage.getBytes(), filename);

            // Create category object
            Category category = new Category();
            category.setCategoryName(categoryName);
            category.setCategoryDescription(categoryDescription);
            category.setCategoryImage(filename);

            // Save the category
            Category newCategory = categoryService.addCategory(category);
            return new ResponseEntity<>(newCategory, HttpStatus.CREATED);

        } catch (IOException e) {
            return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String generateUniqueFilename(String originalFilename) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return "category-" + timestamp + extension;
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Map<String, Object>> updateCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String categoryDescription,
            @RequestParam(value = "categoryImage", required = false) MultipartFile categoryImage) {

        Map<String, Object> response = new HashMap<>();
        try {
            Category updatedCategory = categoryService.updateCategory(categoryId, categoryName, categoryDescription, categoryImage);

            response.put("status", "success");
            response.put("category", updatedCategory);
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

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("categoryId") String categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}


