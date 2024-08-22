package com.abcRestaurant.abcRestaurant.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.abcRestaurant.abcRestaurant.Model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, ObjectId> {
    List<Product> findByCategoryName(String categoryName);
    Optional<Product> findByProductId(String productId);
}
