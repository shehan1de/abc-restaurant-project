package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Category;
import com.abcRestaurant.abcRestaurant.Model.Product;
import com.abcRestaurant.abcRestaurant.Repository.CategoryRepository;
import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import com.abcRestaurant.abcRestaurant.Repository.ProductRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ProductRepository productRepository;

    // Get all categories
    public List<Category> allCategory() {
        return categoryRepository.findAll();
    }

    // Get a single category by id
    public Optional<Category> singleCategory(ObjectId id) {
        return categoryRepository.findById(id);
    }

    // Add a new category
    public Category addCategory(Category category) {
        category.setCategoryId(generateCategoryId());
        return categoryRepository.save(category);
    }

    // ID generation
    private String generateCategoryId() {
        Optional<Category> latestCategory = categoryRepository.findTopByOrderByCategoryIdDesc();
        int maxId = 0;

        if (latestCategory.isPresent()) {
            String categoryId = latestCategory.get().getCategoryId();
            try {
                int numericPart = Integer.parseInt(categoryId.split("-")[1]);
                maxId = numericPart;
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.err.println("Error parsing categoryId: " + categoryId + ". Using default maxId.");
            }
        }

        int nextId = maxId + 1;
        return String.format("category-%03d", nextId);
    }





    public Category updateCategory(String categoryId, String categoryName, String categoryDescription, MultipartFile categoryImage) throws IOException {
        Category existingCategory = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));

        String categoryImageFilename = handleCategoryImageUpload(categoryImage);

        if (categoryName != null && !categoryName.isEmpty()) {
            existingCategory.setCategoryName(categoryName);
        }

        if (categoryDescription != null && !categoryDescription.isEmpty()) {
            existingCategory.setCategoryDescription(categoryDescription);
        }

        if (categoryImageFilename != null) {
            existingCategory.setCategoryImage(categoryImageFilename);
        }

        return categoryRepository.save(existingCategory);
    }

    private String handleCategoryImageUpload(MultipartFile categoryImage) throws IOException {
        if (categoryImage != null && !categoryImage.isEmpty()) {
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String originalFilename = categoryImage.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }
            String categoryImageFilename = "category-" + timestamp + fileExtension;
            fileStorageService.saveFile(categoryImage.getBytes(), categoryImageFilename);
            return categoryImageFilename;
        }
        return null;
    }

    public void deleteCategory(String categoryId) {
        Category existingCategory = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));
        categoryRepository.delete(existingCategory);
    }
}


