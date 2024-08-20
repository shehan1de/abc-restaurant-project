package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Category;
import com.abcRestaurant.abcRestaurant.Repository.CategoryRepository;
import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

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

    // Generate a new category ID
    private String generateCategoryId() {
        long count = categoryRepository.count();
        return String.format("category-%03d", count + 1);
    }

    // Update an existing category by id
    public Category updateCategory(ObjectId id, Category category) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id " + id);
        }
        // Ensure the ID in the request body matches the ID in the URL
        category.setId(id);
        return categoryRepository.save(category);
    }

    // Delete a category by id
    public void deleteCategory(ObjectId id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id " + id);
        }
        categoryRepository.deleteById(id);
    }

}
