package com.abcRestaurant.abcRestaurant.Repository;

import com.abcRestaurant.abcRestaurant.Model.FavoriteProduct;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteProductRepository extends MongoRepository<FavoriteProduct, String> {
    FavoriteProduct findByUserId(String userId);
}
