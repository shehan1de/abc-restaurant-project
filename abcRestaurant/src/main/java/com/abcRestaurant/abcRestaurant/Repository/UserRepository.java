package com.abcRestaurant.abcRestaurant.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.abcRestaurant.abcRestaurant.Model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository <User, ObjectId> {
    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByVerificationCode(String code);
    List<User> findByVerificationCodeExpiryBefore(LocalDateTime expiryDate);


}
