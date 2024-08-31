package com.abcRestaurant.abcRestaurant.Repository;

import com.abcRestaurant.abcRestaurant.Model.Category;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, ObjectId> {
    Optional<Category> findTopByOrderByCategoryIdDesc();
    Optional<Category> findByCategoryId(String categoryId);
}
