package com.abcRestaurant.abcRestaurant.Repository;

import com.abcRestaurant.abcRestaurant.Model.FavoriteProduct;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface FavoriteProductRepository extends MongoRepository<FavoriteProduct, String> {
    FavoriteProduct findByUserId(String userId);
}
